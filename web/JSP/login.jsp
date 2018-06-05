<%-- 
    Document   : loginForm
    Created on : 22 déc. 2016, 12:11:01
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


            <div class="form">
        <div class="container">


                <div class="info">
                    <form method="post" action="login">
                        <h1>Login</h1>
                        <p>Vous devez vous connecter :</p>

                        <div class="row">
                            <input type="text" id="nom" name="nom" value="<c:out value="${ login.nom }"/>" maxlength="45" placeholder="Pseudo"/>
                            <span class="erreur">${erreurs['nom']}</span>
                        </div>

                        <div class="row">
                            <input type="password" id="motdepasse" name="motdepasse" value="<c:out value="${ login.motdepasse }"/>" maxlength="20" placeholder="Mot de passe"/>
                            <span class="erreur">${erreurs['motdepasse']}</span>
                        </div>


                        <input type="submit" value="Login" class="button"/>
                        <p class="${empty erreurs ? 'succes' : 'erreur'}">${message}</p>
                    </form>
                </div>

            </div>
        </div>
    </body>
</html>
