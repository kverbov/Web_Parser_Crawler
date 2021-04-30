package com.mycrawler;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import com.sun.deploy.xml.XMLParser;
import org.w3c.dom.Document; //
import org.w3c.dom.Element;

import static java.awt.SystemColor.window;

public class Main
{


    public static void main(String args[]) {
        HTMLParser htmlParser=new HTMLParser();



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

        HashMap<String, org.jsoup.nodes.Element> content = new HashMap<String, org.jsoup.nodes.Element>();

        ArrayList<String> localUrlList = new ArrayList<String>();
        String keyList[] = {"услуги", "цены", "прайс"};

        //   Берем ссылки
        for (String url : webSites)
            localUrlList.addAll(htmlParser.GetLinks(url, keyList));

        //   Выдергиваем таблицу услуг с ценами
        for (String url : localUrlList) {
            content.put(url, htmlParser.GetContent(url));
        }

        htmlParser.ExportDB(content);

    }


    static String LoadURL(String uri)
    {
        String rez="";
        try {
            // Easiest connection (without authorization)
            URL url = new URL(uri);


            try(InputStreamReader in=new InputStreamReader( url.openStream())) {
                byte [] buf;

                BufferedReader r=new BufferedReader(in);

                //LineNumberReader r = new LineNumberReader(in);
                String str=r.readLine();



                while ((str=r.readLine())!=null) {
                    PrintLn(str);


                }
                r.close();
            } catch (IOException iOException) {
                iOException.printStackTrace();
                return null;
            }

        } catch (MalformedURLException ex) {
            ex.printStackTrace();
            return null;
        }

        return rez;
    }


    //  Вывод в консоль
    static void PrintLn(String str)
    {
        System.out.println(str);
    }



}
