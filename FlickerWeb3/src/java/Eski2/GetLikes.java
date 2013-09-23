/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Eski2;

import static Eski2.GetComment.loadXMLFromString;
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
public class GetLikes {
    //dublicationtag = photoid+likeownerid

    public static void main(String[] args) {


        GetLikes g = new GetLikes();
        g.islemyap();
        // g.updatecommentstatus(15550);
    }

    public void islemyap() {
        int sayac = 1;
        // checkcomments

        try {


            sayac++;
            Connection conn = null;
            Statement st = null;
            ResultSet rs = null;

            Class.forName("com.mysql.jdbc.Driver").newInstance();
            String dbUrl = "jdbc:mysql://omitechnology.com/omitechn_flick?useUnicode=true&characterEncode=UTF-8";
            conn = DriverManager.getConnection(dbUrl, "omite_flick", "flick");
            EscapeChars escapemethod = new EscapeChars();
            st = conn.createStatement();
            rs = st.executeQuery("Select DISTINCT ownerid FROM photos WHERE checklike=0");
            String PhotoOwnerId="61847521@N00";

            while (rs.next()) {
                PhotoOwnerId = rs.getString("ownerid");
                String link = "http://api.flickr.com/services/rest/?method=flickr.favorites.getPublicList&api_key=85eab302afea9e532f665e88916a9415&user_id="+PhotoOwnerId+"&extras=owner_name&per_page=1000000&format=rest";
                getXMLtoSQL(PhotoOwnerId, getXML(link));
           //     System.out.println("oldu");
            }




        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void getXMLtoSQL(String PhotoOwnerId, Document xmlcomment) {
        NodeList nodes = xmlcomment.getElementsByTagName("photo");
        EscapeChars escapemethod = new EscapeChars();
        String photoid;
        String likeownerid;
        String likeownername;
        String date_faved;
        String duplicationtag;

        try {
            Connection conn = null;
            Statement st = null;

            Class.forName("com.mysql.jdbc.Driver").newInstance();
            String dbUrl = "jdbc:mysql://omitechnology.com/omitechn_flick?useUnicode=true&characterEncode=UTF-8";
            conn = DriverManager.getConnection(dbUrl, "omite_flick", "flick");
            conn.createStatement();
            st = conn.createStatement();
          
            System.out.println("nodes.getLength() = " + nodes.getLength());
            for (int i = 0; i < nodes.getLength(); i++) {
                //yorumlarin her biri icin
                Element element = (Element) nodes.item(i);
                photoid = element.getAttribute("id");
              //  System.out.println("photoid = " + photoid);
                likeownerid = element.getAttribute("owner");
              //  System.out.println("likeowner = " + likeownerid);
                
                likeownername = element.getAttribute("ownername");
                likeownername = escapemethod.forHTML(likeownername);
             //   System.out.println("ownername = " + likeownername);
                
                date_faved = element.getAttribute("date_faved");
             //   System.out.println("date_faved = " + date_faved);
          //      System.out.println("\n = ");
                duplicationtag = photoid+likeownerid;
                String insertTableSQL = "INSERT INTO likes"
                        + "(userid,photoid, likeownerid, likeownername,date_faved, duplicationtag) VALUES"
                        + "('" + PhotoOwnerId + "','" + photoid + "','" + likeownerid + "','" + likeownername + "','" + date_faved + "','" + duplicationtag + "')";
            //                System.out.println("insertTableSQL = " + insertTableSQL);
                st.execute(insertTableSQL);
          //     System.out.println("oldu2");


            }
 updatelikestatus(PhotoOwnerId);
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

    public void updatelikestatus(String PhotosOwnerID) {
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            // omitechn_youtubecraw		omite_youtubecra	 ahmet

            Class.forName("com.mysql.jdbc.Driver").newInstance();
            String dbUrl = "jdbc:mysql://omitechnology.com/omitechn_flick?useUnicode=true&characterEncode=UTF-8";
            conn = DriverManager.getConnection(dbUrl, "omite_flick", "flick");
            st = conn.createStatement();
           //     System.out.println("UPDATE photos set checklike=1 WHERE ownerid='" + PhotosOwnerID + "'");
            st.executeUpdate("UPDATE photos set checklike=1 WHERE ownerid='" + PhotosOwnerID + "'");


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
