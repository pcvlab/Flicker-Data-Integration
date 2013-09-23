<%-- 
    Document   : index
    Created on : Sep 14, 2013, 3:06:08 PM
    Author     : amungen
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" import="java.sql.*"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <%

            try {
                Connection conn = null;
                Statement st = null;
                ResultSet rs = null;
                  Class.forName("com.mysql.jdbc.Driver").newInstance();
            String dbUrl = "jdbc:mysql://omitechnology.com/omitechn_flick?useUnicode=true&characterEncode=UTF-8";
            conn = DriverManager.getConnection(dbUrl, "omite_flick", "flick");
            conn.createStatement();
            st = conn.createStatement();
                st = conn.createStatement();
                rs = st.executeQuery("SELECT COUNT(id) FROM comments");
                while (rs.next()) {
        %>
        <b>Comments Sayisi :</b> <%= rs.getInt(1) %> <br>
        <%}
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

        %>


        <%
            try {
                Connection conn = null;
                Statement st = null;
                ResultSet rs = null;
                Class.forName("com.mysql.jdbc.Driver").newInstance();
               
                 String dbUrl = "jdbc:mysql://omitechnology.com/omitechn_flick?useUnicode=true&characterEncode=UTF-8";
            conn = DriverManager.getConnection(dbUrl, "omite_flick", "flick");
         
                
                st = conn.createStatement();
                st = conn.createStatement();
                rs = st.executeQuery("SELECT COUNT(id) FROM likes");
                while (rs.next()) {
        %>
        <b>Likes Sayisi :</b> <%= rs.getInt(1)%> <br>
        <%}
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

        %>



        <%
            try {
                Connection conn = null;
                Statement st = null;
                ResultSet rs = null;
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                String dbUrl = "jdbc:mysql://omitechnology.com/omitechn_flick?useUnicode=true&characterEncode=UTF-8";
            conn = DriverManager.getConnection(dbUrl, "omite_flick", "flick");
          st = conn.createStatement();
                rs = st.executeQuery("SELECT COUNT(id) FROM photos");
                while (rs.next()) {
        %>
        <b>Photos Sayisi :</b> <%= rs.getInt(1)%> <br>
        <%}
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

        %>

    </body>
</html>
