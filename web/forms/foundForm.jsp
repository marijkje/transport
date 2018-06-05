<%@page contentType="text/html" pageEncoding="UTF-8"%>
<c:if test="${googlemap!=null}">
    <div class="info">

        <h1>Géographie</h1>
        <img src="${googlemap}" alt="Pas d'image disponible"/>

    </div>
</c:if>
<div class="info">                   
    <h1><c:out value="${personInfo == 'drivers'?'Clients':'Chauffeurs'}"/> trouvé pour <c:out value="${person.prenom}"/> <c:out value="${person.nom}"/></h1>
    <table>
        <thead>
            <tr>
                <td>Nom</td>
                <td>Prénom</td>
                <td>Distance (km)</td>
                <td>Selection</td>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="pers" items="${distanceList}" varStatus="i">
                <tr>
                    <td><c:out value="${pers.person.nom}"/></td>
                    <td><c:out value="${pers.person.prenom}"/></td>
                    <td><c:out value="${pers.km}"/></td> 
                    <td><input type="checkbox" name="selected" value="<c:out value="${ i.index }"/>"/></td>
                </tr>   
            </c:forEach>
        </tbody> 
    </table>   
</div>
<div class="buttons">
    <input type="submit" name="action" value="Contacter" class="button" formaction="send" formmethod="get"/>
    <input type="submit" name="action" value="Retour" class="button" formaction="drive" formmethod="get"/>
    <p class="${empty erreurs ? 'succes' : 'erreur'}">${message}</p>
</div>
