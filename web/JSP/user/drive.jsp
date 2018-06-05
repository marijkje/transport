<%-- 
    Document   : drive
    Created on : 22 déc. 2016, 12:11:01
    Author     : Mael
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta name="viewport" content="width=device-width, initial-scale=1.0" target-densitydpi=device-dpi/>
        <meta charset="utf-8" />
        <title>Adresse</title>
        <link rel="stylesheet" href="inc/page.css">
        <link rel="stylesheet" href="inc/form.css">
        <link href="https://fonts.googleapis.com/css?family=Ubuntu" rel="stylesheet">
        <link rel="stylesheet" type="text/css" href="inc/js/jquery/jquery.datetimepicker.css">
        <script src="inc/js/jquery/jquery.js"></script>
        <script src="inc/js/jquery/jquery.datetimepicker.full.min.js"></script>        
        <script src="inc/js/driveDate.js"></script>
    </head>
    <body>
        <header>

            <c:import url="/forms/menu.jsp" />

        </header>

        <div class="form">
            <div class="container">
                <form action="drive" method="post">
                    <c:import url="/forms/driveForm.jsp" />
                </form>
            </div>
        </div>
        <script src="inc/js/graySelect.js"></script>

    </body>
</html>
