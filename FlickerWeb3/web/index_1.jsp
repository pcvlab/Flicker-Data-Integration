<%-- 
    Document   : index
    Created on : Sep 12, 2013, 8:29:35 PM
    Author     : amungen
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
<script type="text/javascript">
function loadXMLDoc() {
var xmlhttp=new XMLHttpRequest();
xmlhttp.onreadystatechange=function()
  {     document.getElementById("myDiv").innerHTML=xmlhttp.responseText; }
xmlhttp.open("GET","index.jsp",true);
xmlhttp.send();
setTimeout ( "loadXMLDoc()", 10000 );
}</script> 
        
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Proje Page</title>
    </head>
    Her 10 saniyede 1 guncellenir

    <body onLoad="loadXMLDoc()"><div id="myDiv">Bilgiler Goruntuleniyor</div></body>
      
<br>
    </body>
</html>
