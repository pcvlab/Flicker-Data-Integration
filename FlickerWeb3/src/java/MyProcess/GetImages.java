/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MyProcess;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author amungen
 */
public class GetImages {

    String generalpath = "/Volumes/Levent-Taha/Flicker/";
    public static void main(String[] args) {
        GetImages g = new GetImages();
        g.allprocess("/Volumes/Levent-Taha/Flicker/");
        // g.deleteimage("adadada");
      //  g.settextinf("teesttt");
    }

    public void allprocess(String generalpath) {
    //    for (int i = 1600; i < 3000; i++) {
    //        createnewfolder(i + "");
     //   }
        this.generalpath = generalpath;
       // System.out.println("this.generalpath = " + this.generalpath);
          downloadimages();

    }

    public void downloadimages() {
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            String dbUrl = "jdbc:mysql://omitechnology.com/omitechn_flick?useUnicode=true&characterEncode=UTF-8";
            conn = DriverManager.getConnection(dbUrl, "omite_flick", "flick");
            conn.createStatement();
            st = conn.createStatement();
            int sayac = 0;
            rs = st.executeQuery("Select  url_o,ID,PHOTOID FROM photos WHERE downimage=0 and ID>500000 LIMIT 100");
            System.out.println("query atti");
            int id;
            String folderpath = "1";
            String photoid;
            String link;

            while (rs.next()) {
                id = rs.getInt("id");
                photoid = rs.getString("photoid");
                folderpath = ((id / 500) + 1) + "/";
                link = rs.getString("url_o");
                if (link.length() > 10) {
                    try {

                        singledownloadimage(id, link, folderpath, photoid);

                    } catch (Exception e) {
                    }
                    sayac++;
                    if (sayac % 10 == 0) {
                        System.out.println("Toplam Basarili Download = " + sayac);
                    }
                    if (sayac % 50 == 0) {
                        System.out.println("break = ");
                        settextinf("Basilan ID:" + id + " -  Elli download yapildi");
                        break;

                    }

                }
            }
            conn.close();
            rs.close();
            st.close();
        } catch (Exception e) {
            e.printStackTrace();
        }




    }

    public void settextinf(String sayac) {
        try {
            File file = new File(generalpath + "inf.txt");
            FileWriter fw = new FileWriter(file, true);
            fw.write(sayac + "++++++");
            fw.flush();
            fw.close();

        } catch (Exception e) {
        }

    }

    public void singledownloadimage(int ID, String link, String folderpath, String photoid) {
        try {

            //     System.out.println("link = " + link);
            //   System.out.println("1 = " + link.lastIndexOf("."));
            //   System.out.println("2 = " + link.length());
            String extention = link.substring(link.lastIndexOf("."), link.length());
            // System.out.println("extention = " + extention);
            URL url = new URL(link);
            InputStream in = new BufferedInputStream(url.openStream());
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int n = 0;
            try {
                while (-1 != (n = in.read(buf))) {
                    out.write(buf, 0, n);
                }

            } catch (Exception e) {
                deleteimage(photoid);
                System.out.println("DIKKAT SILINDI LINK " + link);
            }

            out.close();
            in.close();
            byte[] response = out.toByteArray();
            if (response.length > 10000) {
                FileOutputStream fos = new FileOutputStream(generalpath + folderpath + photoid + extention);
                fos.write(response);
                fos.close();
                updateimagestatus(ID);
            } else {
                deleteimage(photoid);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    public void createnewfolder(String foldername) {
        try {
            File newDir = new File(this.generalpath + foldername);
            //File newDir = new File(generalpath + foldername);
            boolean success = newDir.mkdir();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void updateimagestatus(int ID) {
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            // omitechn_youtubecraw		omite_youtubecra	 ahmet

            Class.forName("com.mysql.jdbc.Driver").newInstance();
            String dbUrl = "jdbc:mysql://omitechnology.com/omitechn_flick?useUnicode=true&characterEncode=UTF-8";
            conn = DriverManager.getConnection(dbUrl, "omite_flick", "flick");
            st = conn.createStatement();
            st.executeUpdate("UPDATE photos set downimage=1 WHERE id='" + ID + "'");


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

    public void deleteimage(String imageID) {
        //DELETE FROM photos WHERE photoid='+imageID;
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        try {

            Class.forName("com.mysql.jdbc.Driver").newInstance();
            String dbUrl = "jdbc:mysql://omitechnology.com/omitechn_flick?useUnicode=true&characterEncode=UTF-8";
            conn = DriverManager.getConnection(dbUrl, "omite_flick", "flick");
            st = conn.createStatement();
            String sql = "DELETE FROM photos WHERE photoid='" + imageID + "'";
            // System.out.println("sql = " + sql);
            st.executeUpdate(sql);
            statisticdeletefile();

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

    public void statisticdeletefile() {
        //UPDATE WRONG SET WrongSize = WrongSize + 1 

        Connection conn = null;
        Statement st = null;

        try {


            Class.forName("com.mysql.jdbc.Driver").newInstance();
            String dbUrl = "jdbc:mysql://omitechnology.com/omitechn_flick?useUnicode=true&characterEncode=UTF-8";
            conn = DriverManager.getConnection(dbUrl, "omite_flick", "flick");
            st = conn.createStatement();
            String sqlquery = "UPDATE statistic SET deletedimagesize = deletedimagesize + 1";

            st.execute(sqlquery);
            conn.close();
            st.close();
            //      System.out.println("link yok");
        } catch (Exception e) {
            e.printStackTrace();
        }





    }
}
