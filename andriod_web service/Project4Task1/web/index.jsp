<%-- 
    Document   : result
    Created on : Nov 7, 2016, 8:59:09 PM
    Author     : Jingshiqing
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
     <head>
        <title>Interesting Picture</title>
    </head>
    <body>
        <h1>The flag of <%= request.getAttribute("pictureTag")%></h1><br>
        <img src="<%= request.getAttribute("pictureURL")%>"><br>
        <p>About this flag: <%= request.getAttribute("pictureText")%></p><br/><br/>
         
        <form action="task1" method="GET">
            <label for="letter">Type the word.</label>
            <input type="text" name="searchWord" value="" /><br>
            <input type="submit" value="Submit" />
        </form>
    </body>
</html>
