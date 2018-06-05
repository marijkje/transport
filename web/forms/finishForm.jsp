<%@ page pageEncoding="UTF-8" %>
<c:if test="${what=='nothing'}">
    <form action="finish" method="post">
        <div class="info">
            <h1><p>Demande, Proposition</p><p>ou Archive?</p></h1>
        </div>
        <div class="buttons">
            <input type="submit" name="action" value="Demande" class="button"/>
            <input type="submit" name="action" value="Offre" class="button"/>
            <input type="submit" name="action" value="Archive" class="button"/>
        </div>
    </form>        

</c:if>

<c:if test="${what == 'action'}">
    <form action="finish" method="post">
        <div class="info">
            <h1>Finaliser un transport</h1>
            <c:if test="${driveInfo == 'appeal'}">
            <div class="row">
                <label>
                    <select name="drive" onchange="this.form.submit()">
                        <option value="Sélectionnez un transport" disabled selected style="display:none;"></option> 
                        <c:forEach var="drv" items="${driveList}" varStatus="i">
                            <option value="<c:out value="${i.index}"/>" 
                                    <c:out value="${drv.depart == drive.depart && drv.client.nom == drive.client.nom && drv.client.prenom == drive.client.prenom? 'selected' : ''}"/>>
                                <c:out value="${drv}"/>
                            </option>
                        </c:forEach>
                    </select> 
                </label>
            </div>
            </c:if>
            <c:if test="${driveInfo == 'offer'}">
            <div class="row">
                <label>
                    <select name="drive" onchange="this.form.submit()">
                        <option value="Sélectionnez un transport" disabled selected style="display:none;"></option> 
                        <c:forEach var="drv" items="${driveList}" varStatus="i">
                            <option value="<c:out value="${i.index}"/>" 
                                    <c:out value="${drv.depart == drive.depart && drv.driver.nom == drive.driver.nom && drv.driver.prenom == drive.driver.prenom? 'selected' : ''}"/>>
                                <fmt:formatDate value="${ drv.depart }" pattern="dd/MM/yyyy hh:mm"/>
                                <c:out value="${drv.driver.nom}"/>
                                <c:out value="${drv.driver.prenom}"/>
                                <c:out value="${drv}"/>
                            </option>
                        </c:forEach>
                    </select> 
                </label>
            </div>
            </c:if>
            <c:if test="${driveInfo == 'done'}">
            <div class="row">
                <label>
                    <select name="drive" onchange="this.form.submit()">
                        <option value="Sélectionnez un transport" disabled selected style="display:none;"></option> 
                        <c:forEach var="drv" items="${driveList}" varStatus="i">
                            <option value="<c:out value="${i.index}"/>" 
                                    <c:out value="${drv.depart == drive.depart && drv.driver.nom == drive.driver.nom && drv.driver.prenom == drive.driver.prenom? 'selected' : ''}"/>>
                                <c:out value="${drv}"/>
                            </option>
                        </c:forEach>
                    </select> 
                </label>
            </div>
            </c:if>
            <c:if test="${driveInfo != 'done'}">
            <div class="row">
                <label>
                    <select name="person" onchange="this.form.submit()">
                        <option value="Choisissez la personne accompagnée" disabled selected style="display:none;"></option> 
                        <c:forEach var="pers" items="${personList}" varStatus="i">
                            <option value="<c:out value="${i.index}"/>" 
                                    <c:out value="${person.nom == pers.nom ? 'selected' : ''}"/>>
                                <c:out value="${pers.nom}"/>
                                <c:out value="${pers.prenom}"/>
                            </option>
                        </c:forEach>
                    </select>  
                </label>
            </div>
            </c:if>
        </div>
        <div class="buttons">        
            <c:if test="${confirm == false}">
                <c:if test="${driveInfo != 'done'}"><input type="submit" name="action" value="Finaliser" class="button"/></c:if>
                <input type="submit" name="action" value="Effacer" class="button"/>
                <input type="submit" name="action" value="Retour" class="button"/>
            </c:if>
            <c:if test="${confirm == true}">
                <input type="submit" name="action" value="Confirmer" class="button"/>
            </c:if>
        </div>
    </form>        
</c:if>
<c:if test="${what == 'consult'}">
    <form action="finish" method="post">
        <div class="info">
            <h1>Finaliser un transport</h1>
            <div class="row">
                <label>
                    <select name="done" onchange="this.form.submit()">
                        <option value="Sélectionnez un transport" disabled selected style="display:none;"></option> 
                        <c:forEach var="drv" items="${driveList}" varStatus="i">
                            <c:if test="${personInfo.equals('drivers')}">
                            <option value="<c:out value="${i.index}"/>" 
                                    <c:out value="${drv.depart == drive.depart && drv.driver.nom == drive.driver.nom && drv.driver.prenom == drive.driver.prenom? 'selected' : ''}"/>>
                                <c:out value="${drv.depart}"/>
                                <c:out value="${drv.driver.nom}"/>
                                    <c:out value="${drv.driver.prenom}"/>
                            </option></c:if>
                           <c:if test="${personInfo.equals('clients')}">
                            <option value="<c:out value="${i.index}"/>" 
                                    <c:out value="${drv.depart == drive.depart && drv.client.nom == drive.client.nom && drv.client.prenom == drive.client.prenom? 'selected' : ''}"/>>
                                <c:out value="${drv.depart}"/>
                                <c:out value="${drv.client.nom}"/>
                                    <c:out value="${drv.client.prenom}"/>
                            </option></c:if>
                        </c:forEach>
                    </select> 
                </label>
            </div>
        </div>
        <div class="buttons">        
            <input type="submit" name="action" value="Retour" class="button"/>
        </div>
    </form>        
</c:if>
<p class="${empty erreurs ? 'succes' : 'erreur'}">${message}</p>
