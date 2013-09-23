/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Eski2;

import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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
public class GetData {
    // http://api.flickr.com/services/rest/?method=flickr.photos.getRecent&api_key=d5b4980d80597d42781208fb11cb18b7&extras=description%2C+date_taken%2C+owner_name%2C+geo%2C+tags%2C+o_dims%2C+views%2C+url_o&per_page=2&page=1&format=rest

//
//String insertTableSQL = "INSERT INTO DBUSER"
//		+ "(USER_ID, USERNAME, CREATED_BY, CREATED_DATE) VALUES"
//		+ "(?,?,?,?)";
//PreparedStatement preparedStatement = dbConnection.prepareStatement(insertTableSQL);
//preparedStatement.setInt(1, 11);
//preparedStatement.setString(2, "mkyong");
//preparedStatement.setString(3, "system");
//preparedStatement.setTimestamp(4, getCurrentTimeStamp());
    public static void main(String[] args) {
        GetData d = new GetData();
        //d.islemyap(56, 300);
        // d.authorviaphotostabletophotos();
        d.findphotosbysearch();
    }

    public void islemyap(int baslangic, int bitis) {
        int sayac = baslangic;
        while (sayac < bitis) {
            try {

                String link = "http://api.flickr.com/services/rest/?method=flickr.photos.getRecent&api_key=85eab302afea9e532f665e88916a9415&extras=description%2C+date_taken%2C+owner_name%2C+geo%2C+tags%2C+o_dims%2C+views%2C+url_o&per_page=50&page=" + sayac + "&format=rest";
                //  System.out.println("link = " + link);
                System.out.println("islemsayisi = " + sayac);
                sayac++;
                getXMLtoSQL(getXML(link));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    public void authorviaphotostabletophotos() {
        int sayac = 1;

        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            String dbUrl = "jdbc:mysql://omitechnology.com/omitechn_flick?useUnicode=true&characterEncode=UTF-8";
            conn = DriverManager.getConnection(dbUrl, "omite_flick", "flick");
            conn.createStatement();
            st = conn.createStatement();
            rs = st.executeQuery("SELECT DISTINCT ownerid FROM photos");
            int id;

            while (rs.next()) {
                String ownerid = rs.getString("ownerid");
                System.out.println("ownerid = " + ownerid);
                for (int i = 0; i < 20; i++) {

                    try {
                        String link = "http://api.flickr.com/services/rest/?method=flickr.people.getPublicPhotos&api_key=b5898c941da3c04d790e330f5edd66fa&user_id=" + ownerid + "&extras=description%2C+date_taken%2C+owner_name%2C+geo%2C+tags%2C+o_dims%2C+views%2C+url_o&per_page=1000&page=" + i + "&format=rest";
                        System.out.println("islemsayisi = " + sayac);
                        sayac++;
                        System.out.println("link = " + link);

                        getXMLtoSQL(getXML(link));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void findphotosbysearch() {
        for (int i = 0; i < 60; i++) {
            String link = "http://api.flickr.com/services/rest/?method=flickr.groups.search&api_key=b5898c941da3c04d790e330f5edd66fa&text=image&per_page=100000&page=" + i + "&format=rest";
//group nsid="2067426@N21
            String nsid;
            Document xmlcomment = getXML(link);
            NodeList nodes = xmlcomment.getElementsByTagName("group");
            System.out.println("nodes.get= " + nodes.getLength());
            for (int j = 0; j < nodes.getLength(); j++) {
                Element element = (Element) nodes.item(j);
                nsid = element.getAttribute("nsid");
            //    System.out.println("nsid = " + nsid);
                getphotosfromgroup(nsid);
            }
        }
    }

    public void getphotosfromgroup(String group_id) {
        int sayac = 1;

        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        try {

            for (int i = 0; i < 20; i++) {
                try {
                    String link = " http://api.flickr.com/services/rest/?method=flickr.groups.pools.getPhotos&api_key=b5898c941da3c04d790e330f5edd66fa&group_id=" + group_id + "&extras=description%2C+date_taken%2C+owner_name%2C+geo%2C+tags%2C+o_dims%2C+views%2C+url_o&per_page=100000&page=" + i + "&format=rest";
                    System.out.println("islemsayisi = " + sayac);
                    sayac++;
                    System.out.println("link = " + link);

                    getXMLtoSQL(getXML(link));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getXMLtoSQL(Document xmlcomment) {
        NodeList nodes = xmlcomment.getElementsByTagName("photo");
        EscapeChars escapemethod = new EscapeChars();

        String commendcontent = null;
        String photoid = null;
        String ownerid = null;
        String ownername = null;
        String title = null;
        String datetaken = null;
        String GPS = null;
        String tags = null;
        String views = null;
        int views2;
        String url_o = null;
        String description = null;
        int height_o;
        int width_o;


        int icsayac = 0;
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        try {


            Class.forName("com.mysql.jdbc.Driver").newInstance();
            String dbUrl = "jdbc:mysql://omitechnology.com/omitechn_flick?useUnicode=true&characterEncode=UTF-8";
            conn = DriverManager.getConnection(dbUrl, "omite_flick", "flick");
            conn.createStatement();
            st = conn.createStatement();
            //     String insertTableSQL = "INSERT INTO photos"
            //             + "(photoid, ownerid, ownername, title, datetaken, GPS, views, url_o, description) VALUES"
            //             + "(?,?,?,?,?,?,?,?,?)";
            //     PreparedStatement preparedStatement = conn.prepareStatement(insertTableSQL);

            System.out.println("nodes.get= " + nodes.getLength());
            for (int i = 0; i < nodes.getLength(); i++) {
                //yorumlarin her biri icin
                Element element = (Element) nodes.item(i);
                photoid = element.getAttribute("id");
                ownerid = element.getAttribute("owner");
                ownername = element.getAttribute("ownername");
                title = element.getAttribute("title");
                title = escapemethod.forHTML(title);

                datetaken = element.getAttribute("datetaken");
                // System.out.println("datetaken = " + datetaken);
                GPS = element.getAttribute("latitude") + "//" + element.getAttribute("longitude") + "//" + element.getAttribute("accuracy");
                //   System.out.println("GPS = " + GPS);
                tags = element.getAttribute("tags");
                views = element.getAttribute("views");
                views2 = Integer.parseInt(views);
                url_o = element.getAttribute("url_o");


                if (url_o.length() > 10) {
                    if (element.getAttribute("height_o") != "" && element.getAttribute("width_o") != "") {
                        height_o = Integer.parseInt(element.getAttribute("height_o"));
                        width_o = Integer.parseInt(element.getAttribute("width_o"));

                        if (height_o < 257 || width_o < 257) {
                            incompatiblesize();
                        } else {
                            NodeList name = element.getElementsByTagName("description");
                            Element line = (Element) name.item(0);

                            commendcontent = getCharacterDataFromElement(line);
                            description = escapemethod.forHTML(commendcontent);
                            String insertTableSQL = "INSERT INTO photos"
                                    + "(photoid, ownerid, ownername, title, datetaken, GPS, tags,views, url_o, description) VALUES"
                                    + "('" + photoid + "','" + ownerid + "','" + ownername + "','" + title + "','" + datetaken + "','" + GPS + "','" + tags + "'," + views + ",'" + url_o + "','" + description + "')";
///                            System.out.println("insertTableSQL = " + insertTableSQL);
                            try {

                                st.execute(insertTableSQL);

                                icsayac++;
                                //        preparedStatement.setString(1, photoid);
                                //        preparedStatement.setString(2, ownerid);
                                //        preparedStatement.setString(3, ownername);
                                //        preparedStatement.setString(4, title);
                                //        preparedStatement.setString(5, datetaken);
                                //        preparedStatement.setString(6, GPS);
                                //        preparedStatement.setInt(7, Integer.parseInt(views));
                                //        preparedStatement.setString(8, url_o);
                                //        preparedStatement.setString(9, description);
                                //        preparedStatement.addBatch();

                            } catch (Exception e) {
                            }
                        }
                    }
                } else {
                    notlink();
                }
            }
            System.out.println("Basarili Insert = " + icsayac + "\n");

            // preparedStatement.executeBatch();
        } catch (Exception e) {
            //   e.printStackTrace();
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

    public void incompatiblesize() {
        //UPDATE WRONG SET WrongSize = WrongSize + 1 

        Connection conn = null;
        Statement st = null;

        try {


            Class.forName("com.mysql.jdbc.Driver").newInstance();
            String dbUrl = "jdbc:mysql://omitechnology.com/omitechn_flick?useUnicode=true&characterEncode=UTF-8";
            conn = DriverManager.getConnection(dbUrl, "omite_flick", "flick");
            st = conn.createStatement();
            String sqlquery = "UPDATE statistic SET wrongsize = wrongsize + 1";

            st.execute(sqlquery);
            conn.close();
            st.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void notlink() {
        //UPDATE WRONG SET WrongSize = WrongSize + 1 

        Connection conn = null;
        Statement st = null;

        try {


            Class.forName("com.mysql.jdbc.Driver").newInstance();
            String dbUrl = "jdbc:mysql://omitechnology.com/omitechn_flick?useUnicode=true&characterEncode=UTF-8";
            conn = DriverManager.getConnection(dbUrl, "omite_flick", "flick");
            st = conn.createStatement();
            String sqlquery = "UPDATE statistic SET notlink = notlink + 1";

            st.execute(sqlquery);
            conn.close();
            st.close();
            //      System.out.println("link yok");
        } catch (Exception e) {
            e.printStackTrace();
        }





    }
}
