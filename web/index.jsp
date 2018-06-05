<%-- 
    Document   : index
    Created on : 20 déc. 2016, 17:41:55
    Author     : Mael
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta charset="utf-8" />
        <title>Adresse</title>
        <link rel="stylesheet" href="inc/page.css">
        <link rel="stylesheet" href="inc/form.css">
        <link href="https://fonts.googleapis.com/css?family=Ubuntu" rel="stylesheet">
    </head>
    <body>
        <header>

            <c:import url="/forms/menu.jsp" />
            
        </header>
            <c:if test="${expired}"><p>Votre session est expirée, recommencez.</p></c:if>    
    </body>
</html>
