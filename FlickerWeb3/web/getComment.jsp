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
     <%
         for(int i=0; i<400; i++){
     MyProcess.GetComment gc = new MyProcess.GetComment();
     gc.islemyap();
     MyProcess.GetLikes gl = new MyProcess.GetLikes();
     gl.islemyap();
         }
     %>
    </body>
</html>
