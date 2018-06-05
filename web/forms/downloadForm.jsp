<%@ page pageEncoding="UTF-8" %>
    <form action="downloads" method="post">
        <div class="info">
            <h1>Télécharger un fichier</h1>
            <c:if test="${not empty url}"><a href="${url}" >${message}</a></c:if>
            <c:if test="${empty url}"><p>${message}</p></c:if>
        </div>
        <div class="buttons">
            <input type="submit" name="action" value="Chauffeur" class="button"/>
            <input type="submit" name="action" value="Demandeur" class="button"/>
            <input type="submit" name="action" value="Demandes" class="button"/>
            <input type="submit" name="action" value="Offres" class="button"/>
            <input type="submit" name="action" value="Archive" class="button"/>
        </div>
    </form>        

