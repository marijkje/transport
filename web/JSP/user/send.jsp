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
    </head>
    <body>
        <header>

            <c:import url="/forms/menu.jsp" />

        </header>

        <div class="form">
            <div class="container">
                <form action="send" method="post">
                    <h1>Proposition d'un mail</h1>

                    <c:if test="${empty erreurs['text']}"><textarea name="text" cols="200" rows="10" class="email"><c:out value="${text}"/></textarea></c:if>
                    <c:if test="${not empty erreurs['text']}"><span class="erreur" ><textarea name="text" cols="100" rows="10" class="email"><c:out value="${erreurs['text']}"/></textarea></span></c:if>
                
                <div class="buttons">
                    <c:if test="${send==true}"><input type="submit" name="action" value="Envoyer" class="button"/></c:if>
                    <input type="submit" name="action" value="Retour" class="button"/>
                </form>
                 
            </div>
        </div>

    </body>
</html>
