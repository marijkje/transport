<%@ page pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <meta name="viewport" content="width=device-width, initial-scale=1.0" target-densitydpi=device-dpi/>
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

        <div class="form">
            <div class="container">
                <form action="user" method="post">
                    <c:import url="/forms/userForm.jsp" />
                </form>
            </div>
        </div>
    </body>
</html>