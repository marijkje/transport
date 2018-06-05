<%@ page pageEncoding="UTF-8" %>
<c:if test="${not found}">
    <div class="info">
        <h1>Trajet <c:out value="${personInfo == 'clients'?'Demandé':'Proposé'}"/></h1>
        <div class="row">
            <select id="searchp" name="searchp" onchange="this.form.submit()">
                <option value="" disabled selected style="display:none;">Choisissez un <c:out value="${personInfo == 'clients'?'demandeur':'chauffeur'}"/></option> 
                <c:forEach var="pers" items="${personList}" varStatus="i">
                    <option value="<c:out value="${i.index}"/>" 
                            <c:out value="${person.nom == pers.nom && pers.prenom == person.prenom ? 'selected' : ''}"/>>
                        <c:out value="${pers.nom}"/>
                        <c:out value="${pers.prenom}"/>
                    </option>
                </c:forEach>
            </select>  
        </div>
        <div class="row">
            <select id="searchd" name="searchd" onchange="this.form.submit()">
                <option value="" disabled selected style="display:none;">Choisissez <c:out value="${driveInfo == 'appeal'?'une demande':'un offre'}"/></option> 
                <c:forEach var="drv" items="${driveList}" varStatus="i">
                    <c:if test="${driveInfo == 'offer'}">
                    <option value="<c:out value="${i.index}"/>" 
                            <c:out value="${drive.depart == drv.depart && drive.driver.nom == drv.driver.nom && drive.driver.prenom == drv.driver.prenom ? 'selected' : ''}"/>>
                        <c:out value="${drv.depart}"/>
                        <c:out value="${drv.driver.nom}"/>
                        <c:out value="${drv.driver.prenom}"/>
                    </option></c:if>
                    <c:if test="${driveInfo == 'appeal'}">
                    <option value="<c:out value="${i.index}"/>" 
                            <c:out value="${drive.depart == drv.depart && drive.client.nom == drv.client.nom && drive.client.prenom == drv.client.prenom ? 'selected' : ''}"/>>
                        <c:out value="${drv.depart}"/>
                        <c:out value="${drv.client.nom}"/>
                        <c:out value="${drv.client.prenom}"/>
                    </option></c:if>
                </c:forEach>
            </select>  
        </div>
        <div class="row">
            <c:if test="${empty erreurs['adresseDepart']}"><input type="text" id="adresseDepart" name="adresseDepart" value="<c:out value="${ drive.adresseDepart }"/>" maxlength="45"  placeholder="Rue ou Lieu-dit Départ"/></c:if>
            <c:if test="${not empty erreurs['adresseDepart']}"><div class="erreur"><input type="text" id="adresseDepart" name="adresseDepart" value="<c:out value="${erreurs['adresseDepart']}"/>" maxlength="45" placeholder="Rue ou Lieu-dit Départ"/></div></c:if>
        </div>
        <div class="row">
            <c:if test="${empty erreurs['codeDepart']}"><input type="text" id="codeDepart" name="codeDepart" value="<c:out value="${ drive.codeDepart }"/>"  maxlength="20" placeholder="Code Postale Départ"/></c:if>
            <c:if test="${not empty erreurs['codeDepart']}"><div class="erreur"><input type="text" id="codeDepart" name="codeDepart" value="<c:out value="${erreurs['codeDepart']}"/>"  maxlength="20" placeholder="Code Postale Départ"/></div></c:if>
        </div>
        <div class="row">
            <c:if test="${empty erreurs['villeDepart']}"><input type="text" id="villeDepart" name="villeDepart" value="<c:out value="${ drive.villeDepart }"/>" maxlength="45" placeholder="Commune Départ"/></c:if>
            <c:if test="${not empty erreurs['villeDepart']}"><div class="erreur"><input type="text" id="villeDepart" name="villeDepart" value="<c:out value="${erreurs['villeDepart']}"/>" maxlength="45" placeholder="Commune Départ"/></div></c:if>
        </div>
        <div class="row">
            <c:if test="${empty erreurs['adresseArrivee']}"><input type="text" id="adresseArrivee" name="adresseArrivee" value="<c:out value="${ drive.adresseArrivee }"/>" maxlength="45" placeholder="Rue ou Lieu-dit Arrivée"/></c:if>
            <c:if test="${not empty erreurs['adresseArrivee']}"><div class="erreur"><input type="text" id="adresseArrivee" name="adresseArrivee" value="<c:out value="${erreurs['adresseArrivee']}"/>" maxlength="45" placeholder="Rue ou Lieu-dit Arrivée"/></div></c:if>
        </div>
        <div class="row">
            <c:if test="${empty erreurs['codeArrivee']}"><input type="text" id="codeArrivee" name="codeArrivee" value="<c:out value="${ drive.codeArrivee }"/>"  maxlength="20" placeholder="Code Postale Arrivée"/></c:if>
            <c:if test="${not empty erreurs['codeArrivee']}"><div class="erreur"><input type="text" id="codeArrivee" name="codeArrivee" value="<c:out value="${erreurs['codeArrivee']}"/>"  maxlength="20" placeholder="Code Postale Arrivée"/></div></c:if>
        </div>
        <div class="row">
            <c:if test="${empty erreurs['villeArrivee']}"><input type="text" id="villeArrivee" name="villeArrivee" value="<c:out value="${ drive.villeArrivee }"/>" maxlength="45" placeholder="Commune Arrivée"/></c:if>
            <c:if test="${not empty erreurs['villeArrivee']}"><div class="erreur"><input type="text" id="villeArrivee" name="villeArrivee" value="<c:out value="${erreurs['villeArrivee']}"/>" maxlength="45" placeholder="Commune Arrivée"/></div></c:if>
        </div>
    </div>
    <div class="info">
        <h1>Date <c:out value="${personInfo == 'clients'?'Demandé':'Proposé'}"/></h1>
        <div class="row">
            <label>Départ:</label>
            <c:if test="${empty erreurs['dateDepart']}"><input class="medium" type="text" id="StartDate" name = "depart" value = "<fmt:formatDate value="${ drive.depart }" 
                            pattern="dd/MM/yyyy HH:mm" />" placeholder="jj/mm/aaaa hh:mm" /></c:if>
            <c:if test="${not empty erreurs['dateDepart']}"><div class="erreur">
                <input class="medium" type="text" id="StartDate" name = "depart" value = "<fmt:formatDate value="${ drive.depart }" 
                            pattern="dd/MM/yyyy HH:mm" />" placeholder="${erreurs['dateDepart']}"/> 
            </div></c:if>
        </div>    
        <div class="row">
            <label>Retour:</label>
            <c:if test="${empty erreurs['dateRetour']}"><input class="medium" type="text" id="EndDate" name = "retour" value = "<fmt:formatDate value="${ drive.retour }" 
                            pattern="dd/MM/yyyy HH:mm" />" placeholder="jj/mm/aaaa hh:mm" /></c:if>
            <c:if test="${not empty erreurs['dateRetour']}"><div class="erreur">
                <input class="medium" type="text" id="EndDate" name = "retour" value = "<fmt:formatDate value="${ drive.retour }" 
                                pattern="dd/MM/yyyy HH:mm" />" placeholder="${erreurs['dateRetour']}"/></div>
            </c:if>
        </div>
        <div class="row">
            <h2>Flexibilité :</h2>
            <label>heures :</label>
            <input class="small" type="text" name="flexHeure" value="<c:out value="${ drive.flexHeure }"/>" maxlength="45" placeholder="Flexibilité Heure"/>
        </div>
        <div class="row">
            <label>jours :</label>
            <input class="small" type="text" name="flexDate" value="<c:out value="${ drive.flexDate }"/>" maxlength="45" placeholder="Flexibilité Date"/>
        </div>
        <div class="row">
            <label>Nombre de places :</label>
            <input class="small" type="text" id="places" name="places" value="<c:out value="${ drive.places }"/>" maxlength="45" placeholder="Nombres de places"/>
        </div>
        <div class="row">
            <input type="text" id="motif" name="motif" value="<c:out value="${ drive.motif }"/>" maxlength="45" placeholder="Motif du transport"/>
        </div>
    </div>
    <c:if test="${googlemap!=null}">
        <div class="info">

            <h1>Géographie</h1>
            <img src="${googlemap}" alt="Pas d'image disponible"/>
            <!--remarques-->

        </div>
    </c:if>
    <div class="buttons">
        <input type="submit" name="action" value="Trouver" class="button"/>
        <input type="submit" name="action" value="Vider" class="button"/>
        <p class="${empty erreurs ? 'succes' : 'erreur'}">${message}</p>
    </div>
</c:if>
<c:if test="${found}">
    <c:import url="/forms/foundForm.jsp" />
</c:if>