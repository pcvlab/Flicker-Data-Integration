<%-- 
    Document   : getPhotos
    Created on : Sep 14, 2013, 4:39:45 PM
    Author     : amungen
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        ISLEM BASLADI
        <%
            int sayac =0;
            while (sayac<999999) {
                sayac++;
                try {
                    MyProcess.GetLikes gc = new MyProcess.GetLikes();
                    gc.islemyap();
                } catch (Exception o) {
                    %>
                    ISLEM BITTI
                    <%
                }
            }
        %>
    </body>
</html>
