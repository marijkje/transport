<%@ page pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <meta name="viewport" content="width=device-width, initial-scale=1.0" target-densitydpi=device-dpi/>
        <meta charset="utf-8" />
        <title>Downloads</title>
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
                <form method="post" action="download">
                    <c:import url="/forms/downloadForm.jsp" />
                </form>
            </div>
        </div>

    </body>
</html>