import java.io.File;
import java.io.IOException;
import java.security.PublicKey;
import java.sql.*;
import java.sql.Connection;
import java.sql.Statement;
import java.util.*;

import com.sun.xml.internal.bind.v2.runtime.output.NamespaceContextImpl;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/*
Основной класс парсера услуг с сайтов
 */
public class HTMLParser {


    public static void main(String args[]) {

        String[] webSites = {
                "http://super-servise.ru"
//                "http://pchlp.ru",
//                "http://www.mositservice.ru",
//                "https://www.compulog.ru",
//                "https://computer-master.org/",
//                "http://komp-servis.club/",
//                "http://www.win911.ru/",
//                "http://remontpk.com/",
//                "https://www.compulog.ru/",
//                "http://maxcomputer.ru/",
//                "http://remit-service.ru/",
//                "http://comp-ag.ru/",
//                "http://pc-remontmoskva.ru/",
//                "http://komp-pomosch.ru/",
//                "http://pedant.ru/remont-computerov",
//                "http://www.servicecomp.ru/",
//                "http://helpanu.ru/katalog/remont-kompjuterov.html",
//                "http://comphelpmoscow.ru/srochnyj-remont-kompyutera",
//                "http://servicenadom.ru/",
//                "http://leader-comp.ru/",
//                "http://comprayexpress.ru/",
//                "http://injeneer.ru/"
        };

        HashMap<String, Element> content = new HashMap<String, Element>();

        ArrayList<String> localUrlList = new ArrayList<String>();
        String keyList[] = {"услуги", "цены", "прайс"};

        //   Берем ссылки
        for (String url : webSites)
            localUrlList.addAll(GetLinks(url, keyList));

        //   Выдергиваем таблицу услуг с ценами
        for (String url : localUrlList) {
            content.put(url, GetContent(url));
        }

        ExportDB(content);

    }

    static class TableRow
    {
        public String[] TableRow(String a, String b, String c)
        {
            return new String[3];

        }
    }

    static boolean ExportDB(HashMap<String, Element> content) {
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
            String url, html;
            while (it.hasNext()) {
                Element el = it.next();
                url = el.baseUri();
                html = el.html();
                ParseTable(html);
                String queryText = "insert into services values ('";//+url+ ",'"+   +   ')";
                state.executeUpdate(queryText);
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

    static  void ParseTable(String html)
    {
        ArrayList<String> row=new ArrayList<String>();
        row.add("aaa");
        row.add("bbb");
        ArrayList<ArrayList<String>> array=new ArrayList<ArrayList<String>>();
        array.add(row);


        //ArrayList<List<String>> table=new ArrayList<TableRow>();




    }

    static Element GetContent(String pageUrl) {
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

    static ArrayList<String> GetLinks(String siteUrl, String[] keyList) {
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

    static void PrintLn(Object str) {
        System.out.println(str);
    }

    public static void main_backup(String args[]) {

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