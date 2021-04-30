package com.mycrawler;

import java.io.File;
import java.io.IOException;
import java.security.PublicKey;
import java.sql.*;
import java.sql.Connection;
import java.sql.Statement;
import java.util.*;

import com.sun.org.apache.bcel.internal.generic.INSTANCEOF;
import com.sun.xml.internal.bind.v2.runtime.output.NamespaceContextImpl;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/*
Основной класс парсера услуг с сайтов
 */
public class HTMLParser {

    public boolean ExportDB(HashMap<String, Element> content) {
        ResultSet res = null;
//       try {
//           Class.forName("com.mysql.jdbc.Driver");
//       }
//       catch (ClassNotFoundException e) {
//           // TODO Auto-generated catch block
//           e.printStackTrace();
//       }

        try {

            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mysql", "root", "delphin");

            Statement state = con.createStatement();

            Iterator<Element> it = content.values().iterator();
            ArrayList<Row> rowList=new ArrayList<>();

            //  Формируем таблицу услуг для экспорта в БД
            while (it.hasNext()) {
                Element el = it.next();
                String url = el.baseUri();
                rowList=ParseTable(el);

                //  Запись строк таблицы в БД
                for(Row row:rowList)
                {
                    String queryText = "insert into services values ('"+url+ "','"+  row.Get(0)+"','" + row.Get(1)+  "')";
                    state.executeUpdate(queryText);
                }
            }

//           for (Map.Entry entry : content.entrySet())
//           {
//               System.out.println("Key: " + entry.getKey() + " Value: "
//                   + entry.getValue());
//           }

            while (res.next())
                PrintLn(res.getString("id") + "\n" + res.getString("url") + "\n" + res.getString("html") + "\n");

        } catch (SQLException e) {
            PrintLn(e.getMessage());
            return false;

        }

        return true;
    }

     class Row
    {
        private String [] row;

        public  Row ( String service, String price)
        {
            row=new String[3];
            row[0]=service;
            row[1]=price;
        }

        public String Get(int index)
        {
            switch (index)
            {
                case 0:
                    return row[index];
                case 1:
                    return row[index];
                default:
                    return "Bad Index"+String.valueOf(index);
            }
        }



    }
    
      ArrayList<Row> ParseTable(Element html)
    {
        ArrayList<Row> rezTable=new ArrayList<>();
        Elements trList=html.getElementsByTag("tr");
        Elements tdList=html.getElementsByTag("td");

        //  вычисляем кол-во строк, содержащих 2 столбца
        int rowCount=trList.size()- (trList.size()*2-tdList.size())/2-1;

        Iterator<Element> it= tdList.iterator();
        for( int i=0;it.hasNext() &&i<rowCount;i++ )
        {
            String [] row=new String[2];
            Element el=it.next();
            row[0]=el.text();

            if(it.hasNext())
            {
                el=it.next();
                row[1]=el.text();
            }
            else
                row[1]="END of Table";

            rezTable.add(new Row(row[0],row[1]));

        }

        //ArrayList<List<String>> table=new ArrayList<TableRow>();
        return rezTable;
    }

     public Element GetContent(String pageUrl) {
        Element rez = null;
        Document doc = null;

        try {
            doc = Jsoup.connect(pageUrl).get();

        } catch (Exception e) {

        }

        Elements tables = doc.select("table");
        Iterator<Element> iterator = tables.iterator();
        while (iterator.hasNext()) {
            Element tmp = iterator.next();
            String itext = tmp.text();
            if (itext.toLowerCase().contains("стоимость") ||
                    itext.toLowerCase().contains("цена") ||
                    itext.toLowerCase().contains("руб"))
                rez = tmp;

        }

        return rez;
    }

     public ArrayList<String> GetLinks(String siteUrl, String[] keyList) {
        Document doc = null;
        ArrayList<String> rez = new ArrayList<String>();
        try {
            doc = Jsoup.connect(siteUrl).get();

        } catch (Exception e) {

        }

        Elements links = doc.select("a[href]");
        for (Element el : links) {
            for (String s : keyList) {
                if (el.text().toLowerCase().contains(s)) {
                    String url = el.tagName("a").attr("href");
                    if (!url.contains("http"))
                        url = siteUrl + "/" + url;
                    url = url.replace("//", "/").replace(":/", "://");
                    if (!rez.contains(url))
                        rez.add(url);
                }

            }
        }
        return rez;
        //Elements forms = doc.select(".form");
    }

     void PrintLn(Object str) {
        System.out.println(str);
    }

    public  void main_backup(String args[]) {

        // Parse HTML String using JSoup library
        String HTMLSTring = "<!DOCTYPE html>"
                + "<html>"
                + "<head>"
                + "<title>JSoup Example</title>"
                + "</head>"
                + "<body>"
                + "<table><tr><td><h1>HelloWorld</h1></tr>"
                + "</table>"
                + "</body>"
                + "</html>";

        Document html = Jsoup.parse(HTMLSTring);
        String title = html.title();
        String h1 = html.body().getElementsByTag("h1").text();

        System.out.println("Input HTML String to JSoup :" + HTMLSTring);
        System.out.println("After parsing, Title : " + title);
        System.out.println("Afte parsing, Heading : " + h1);

        // JSoup Example 2 - Reading HTML page from URL
        Document doc;
        try {
            doc = Jsoup.connect("http://mail.ru/").get();
            String s = doc.text();
            title = doc.normalise().title();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Jsoup Can read HTML page from URL, title : " + title);

        // JSoup Example 3 - Parsing an HTML file in Java
        //Document htmlFile = Jsoup.parse("login.html", "ISO-8859-1"); // wrong
        Document htmlFile = null;
        try {
            htmlFile = Jsoup.parse(new File("login.html"), "ISO-8859-1");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } // right
        title = htmlFile.title();
        Element div = htmlFile.getElementById("login");
        String cssClass = div.className(); // getting class form HTML element

        System.out.println("Jsoup can also parse HTML file directly");
        System.out.println("title : " + title);
        System.out.println("class of div tag : " + cssClass);
    }
}