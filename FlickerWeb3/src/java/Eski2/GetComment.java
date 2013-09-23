/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Eski2;

import static Eski2.GetComment.loadXMLFromString;
import static Eski2.GetData.getCharacterDataFromElement;
import static Eski2.GetData.loadXMLFromString;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 *
 * @author amungen
 */
public class GetComment {

    public static void main(String[] args) {
        GetComment g = new GetComment();
        g.islemyap();
       // g.updatecommentstatus(15550);
    }

    public void islemyap() {
        int sayac = 1;
        // checkcomments

        try {

            String photo_id;
            
            sayac++;
            Connection conn = null;
            Statement st = null;
            ResultSet rs = null;

            Class.forName("com.mysql.jdbc.Driver").newInstance();
            String dbUrl = "jdbc:mysql://omitechnology.com/omitechn_flick?useUnicode=true&characterEncode=UTF-8";
            conn = DriverManager.getConnection(dbUrl, "omite_flick", "flick");
            EscapeChars escapemethod = new EscapeChars();
            st = conn.createStatement();
            rs = st.executeQuery("Select id,PHOTOID FROM photos WHERE checkcomments=0");
            int id;

            while (rs.next()) {
//System.out.println("islemsayisi = " + sayac);
                id = rs.getInt("id");
               System.out.println("id = " + id);
                photo_id = rs.getString("photoid");
                String link = "http://api.flickr.com/services/rest/?method=flickr.photos.comments.getList&api_key=85eab302afea9e532f665e88916a9415&photo_id=" + photo_id + "&format=rest";
            //    System.out.println("test1");
                try {
                    
                getXMLtoSQL(id, photo_id, getXML(link));
                
                } catch (Exception e) {
                }
                System.out.println("test2");
            }




        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void getXMLtoSQL(int ID, String photo_id, Document xmlcomment) {
        NodeList nodes = xmlcomment.getElementsByTagName("comment");
        EscapeChars escapemethod = new EscapeChars();
        String commentid;
        String author;
        String authorname;
        String datecreate;
        String path_alias;
        String realname;
        String comment;
        try {
        Connection conn = null;
        Statement st = null;
       


            Class.forName("com.mysql.jdbc.Driver").newInstance();
            String dbUrl = "jdbc:mysql://omitechnology.com/omitechn_flick?useUnicode=true&characterEncode=UTF-8";
            conn = DriverManager.getConnection(dbUrl, "omite_flick", "flick");
            conn.createStatement();
            st = conn.createStatement();
              
            for (int i = 0; i < nodes.getLength(); i++) {
                //yorumlarin her biri icin
                Element element = (Element) nodes.item(i);
                commentid = element.getAttribute("id");
                author = element.getAttribute("author");
                authorname = element.getAttribute("authorname");
                datecreate = element.getAttribute("datecreate");
                path_alias = element.getAttribute("path_alias");
                realname = element.getAttribute("realname");
                comment = element.getTextContent();
                comment = escapemethod.forHTML(comment);
                if (comment.length() > 5000) {
                    comment = comment.substring(0, 4998);
                }
                String insertTableSQL = "INSERT INTO comments"
                        + "(commentid,photo_id, author, authorname, datecreate, path_alias, realname, comment) VALUES"
                        + "('" + commentid + "','" + photo_id + "','" + author + "','" + authorname + "','" + datecreate + "','" + path_alias + "','" + realname + "','" + comment + "')";
//                            System.out.println("insertTableSQL = " + insertTableSQL);
           
                //     System.out.println("insertTableSQL = " + insertTableSQL);
                st.execute(insertTableSQL);
             //   System.out.println("oldu!!");
            }

            updatecommentstatus(ID);
         
         
            st.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Document getXML(String link) {
        String content;
        Document xmlcomment = null;
        try {
            URLConnection connection = null;
            connection = new URL(link).openConnection();
            Scanner scanner = new Scanner(connection.getInputStream());
            scanner.useDelimiter("\\Z");
            content = scanner.next();

            xmlcomment = loadXMLFromString(content);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return xmlcomment;
    }

    public void updatecommentstatus(int ID) {
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            // omitechn_youtubecraw		omite_youtubecra	 ahmet

            Class.forName("com.mysql.jdbc.Driver").newInstance();
            String dbUrl = "jdbc:mysql://omitechnology.com/omitechn_flick?useUnicode=true&characterEncode=UTF-8";
            conn = DriverManager.getConnection(dbUrl, "omite_flick", "flick");
            st = conn.createStatement();
        //    System.out.println("UPDATE photos set checkcomments=1 WHERE id='" + ID + "'");
            st.executeUpdate("UPDATE photos set checkcomments=1 WHERE id='" + ID + "'");


            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
            if (conn != null) {
                conn.close();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static Document loadXMLFromString(String xml) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xml));
        return builder.parse(is);
    }

    public static String getCharacterDataFromElement(Element e) {
        Node child = e.getFirstChild();
        if (child instanceof CharacterData) {
            CharacterData cd = (CharacterData) child;
            return cd.getData();
        }
        return "?";
    }
}
