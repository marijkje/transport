<%@ page pageEncoding="UTF-8" %>
<c:if test="${what=='nothing'}">
<form action="finish" method="post">
    <div class="info">
        <div class="row">
            <label>
                <select name="drive" onchange="this.form.submit()">
                    <option value="Sélectionnez un transport" disabled selected style="display:none;"></option> 
                    <c:forEach var="drv" items="${driveList}" varStatus="i">
                        <option value="<c:out value="${i.index}"/>" 
                                <c:out value="${drv.depart == drive.depart && drv.driver.nom == drive.driver.nom && drv.driver.prenom == drive.driver.prenom? 'selected' : ''}"/>>
                            <c:out value="${drv.depart}"/>
                            <c:out value="${drv.driver.nom}"/>
                            <c:out value="${drv.driver.prenom}"/>
                            <c:out value="${personList[i].nom}"/>
                        </option>
                    </c:forEach>
                </select> 
            </label>
        </div>
    </div>
</form>
</c:if>
<c:if test="${what == 'action'}">
    
</c:if>
<form action="finish" method="post">
    <div class="buttons">        
        <input type="submit" name="action" value="Chercher" class="button"/>
        <input type="submit" name="action" value="Retour" class="button"/>
    </div>
</form>        
<p class="${empty erreurs ? 'succes' : 'erreur'}">${message}</p>
