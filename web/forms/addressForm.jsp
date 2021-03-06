<%@ page pageEncoding="UTF-8" %>



<div class="info">

    <h1>Le <c:out value="${personInfo == 'clients'?'Demandeur':'Chauffeur'}"/></h1>

    <div class="row">
        <label>
            <select name="search" onchange="this.form.submit()" >
                <option value="" selected disabled style="display:none;">Selectionnez une personne</option> 
                <c:forEach var="pers" items="${personList}" varStatus="i">
                    <option value="<c:out value="${i.index}"/>" 
                            <c:out value="${pers.nom == person.nom && pers.prenom == person.prenom ? 'selected' : ''}"/>>
                        <c:out value="${pers.nom}"/>
                        <c:out value="${pers.prenom}"/>
                    </option>
                </c:forEach> 
            </select>
        </label>
    </div>

    <!--nom-->
    <div class="row">
        <c:if test="${empty erreurs['nom']}"><input type="text" id="nom" name="nom" value="<c:out value="${ person.nom }"/>" maxlength="45" placeholder="Nom"/></c:if>
        <c:if test="${not empty erreurs['nom']}"><input class="erreur" type="text" id="nom" name="nom" value="" maxlength="45" placeholder="<c:out value="${ erreurs['nom'] }"/>"/></c:if>
    </div>

    <!--prénom-->
    <div class="row">
        <select name="civilite" class="tiny">
            <option <c:out value="${person.civilite == 'Mme'?'selected':''}"/>>Mme</option>
            <option <c:out value="${person.civilite == 'M.'?'selected':''}"/>>M.</option>
        </select>
        <c:if test="${empty erreurs['prenom']}"><input type="text" id="prenom" name="prenom" value="<c:out value="${ person.prenom }"/>" maxlength="45" placeholder="Prenom"/></c:if>
        <c:if test="${not empty erreurs['prenom']}"><input class="erreur" type="text" id="prenom" name="prenom" value="" maxlength="45" placeholder="<c:out value="${erreurs['prenom']}"/>"/></c:if>
    </div>

    <!--rue ou lieu-dit-->
    <div class="row">
        <c:if test="${empty erreurs['adresse']}"><input type="text" id="adresse" name="adresse" value="<c:out value="${ person.adresse }"/>" maxlength="45" placeholder="Rue ou Lieu-dit"/></c:if>
        <c:if test="${not empty erreurs['adresse']}"><input class="erreur" type="text" id="adresse" name="adresse" value="" maxlength="45" placeholder="<c:out value="${ erreurs['adresse'] }"/>"/></c:if>
    </div>

    <!--code postal-->
    <div class="row">
        <c:if test="${empty erreurs['code']}"><input type="text" id="code" name="code" value="<c:out value="${ person.code }"/>"  maxlength="20" placeholder="Code Postal"/></c:if>
        <c:if test="${not empty erreurs['code']}"><input class="erreur" type="text" id="code" name="code" value=""  maxlength="20" placeholder="<c:out value="${erreurs['code']}"/>"/></c:if>
    </div>

    <!--commune-->
    <div class="row">
        <c:if test="${empty erreurs['ville']}"><input type="text" id="ville" name="ville" value="<c:out value="${ person.ville }"/>" maxlength="45" placeholder="Commune"/></c:if>
        <c:if test="${not empty erreurs['ville']}"><input class="erreur" type="text" id="ville" name="ville" value="" maxlength="45" placeholder="<c:out value="${erreurs['ville']}"/>"/></c:if>
    </div>

    <!--telephone-->
    <div class="row">
        <c:if test="${empty erreurs['tel']}"><input type="tel" id="tel" name="tel" value="<c:out value="${ person.tel }"/>"  maxlength="20" placeholder="Téléphone"/></c:if>
        <c:if test="${not empty erreurs['tel']}"><div class="erreur"><input type="tel" id="tel" name="tel" value=""  maxlength="20" placeholder="<c:out value="${erreurs['tel']}"/>"/></div></c:if>
    </div>

    <!--email-->
    <div class="row">
        <c:if test="${empty erreurs['email']}"><input type="email" id="email" name="email" value="<c:out value="${ person.email }"/>" maxlength="45" placeholder="Email"/></c:if>
        <c:if test="${not empty erreurs['email']}"><div class="erreur"><input type="email" id="email" name="email" value="" maxlength="45" placeholder="<c:out value="${erreurs['email']}"/>"/></div></c:if>
        
    </div>

    <!--année de naissance-->
    <div class="row">
        <label>Année de naissance:</label>
        <select name="naissance" class="tiny">
            <fmt:formatDate value="${ person.naissance }" pattern="yyyy" var = "byear"/>
            <c:forEach items="${years}" var="year">
                <option <c:out value = "${byear == year?'selected':''}"/>>
                    <c:out value="${year}"/></option>
            </c:forEach>
        </select>
    </div>    
    <c:if test="${personInfo=='drivers'}">
        <div class="row">
            <c:if test="${empty erreurs['remarques']}"><textarea name="remarques" placeholder="Remarques" cols="50" rows="4" ><c:out value="${person.remarques}" /></textarea></c:if>
            <c:if test="${not empty erreurs['remarques']}"><div class="erreur"><textarea name="remarques" placeholder="Remarques" cols="50" rows="4" ><c:out value="${erreurs['remarques']}" /></textarea></div></c:if>
        </div>
    </c:if>
</div>

<div class="info">

    <c:if test="${personInfo=='drivers'}">
        <h1>Disponibilités</h1>

        <c:forEach  items="${days.split(',')}" varStatus="i" var="day" >
            <div class="row">
                <c:set target="${person.dispo}" property="jour" value="${day}" />
                <div class="label">
                    <label><c:out value="${day}"/></label>
                </div>
                <div class="input">
                    <select name="<c:out value="${day}1"/>" class="tiny">
                        <c:forEach begin="7" end="22" var="loop">
                            <option <c:out value="${person.dispo.jour.split(',')[0] == loop?'selected':''}"/>>
                                <c:out value="${loop}"/></option>
                        </c:forEach>
                    </select>
                    <select name="<c:out value="${day}2"/>" class="tiny">
                        <c:forEach begin="7" end="22" var="loop">
                            <option <c:out value="${person.dispo.jour.split(',')[1] == loop?'selected':''}"/>>
                                <c:out value="${loop}"/></option>
                        </c:forEach>
                    </select>
                </div>
            </div>
        </c:forEach>
        <!--places-->
        <div class="row">
            <label>Places disponibles: </label>
            
            <select name="places" class="tiny">
                <c:forEach begin="1" end="8" var="loop">
                <option <c:out value="${person.places == loop?'selected':''}"/>>
                   <c:out value="${loop}"/></option>
                </c:forEach>
            </select>    

        </div>
        
    </c:if>
    <c:if test="${personInfo=='clients'}">
        <h1>Contact et Remarques</h1>
    
        <!--contact-->
        <div class="row">
            <c:if test="${empty erreurs['contact']}"><input type="text" id="contact" name="contact" value="<c:out value="${ person.contact }"/>"  maxlength="45" placeholder="Nom et Prénom contact"/></c:if>
            <c:if test="${not empty erreurs['contact']}"><div class="erreur"><input type="tel" id="contact" name="contact" value=""  maxlength="45" placeholder="<c:out value="${ erreurs['contact'] }"/>"/></div></c:if>
        </div>

        <!--telephone contact-->
        <div class="row">
            <c:if test="${empty erreurs['contacttel']}"><input type="tel" id="contacttel" name="contacttel" value="<c:out value="${ person.contacttel }"/>"  maxlength="20" placeholder="Téléphone contact"/></c:if>
            <c:if test="${not empty erreurs['contacttel']}"><div class="erreur"><input type="tel" id="contacttel" name="contacttel" value=""  maxlength="20" placeholder="<c:out value="${ erreurs['contacttel'] }"/>"/></div></c:if>
        </div>

        <div class="row">
            <c:if test="${empty erreurs['remarques']}"><textarea name="remarques" placeholder="Remarques" cols="50" rows="4" ><c:out value="${person.remarques}" /></textarea></c:if>
            <c:if test="${not empty erreurs['remarques']}"><div class="erreur"><textarea name="remarques" placeholder="Remarques" cols="50" rows="4" ><c:out value="${erreurs['remarques']}" /></textarea></div></c:if>
        </div>
    </c:if>

    <!--nombre-->
    <div class="row">
        <label>Transports effectués: </label><c:out value="${ person.nombre }" />
    </div>
        
    <div class="row">
        <label>Charte signé :  </label>
        <input type="checkbox" name="signed" <c:out value="${person.charte?'checked':''}" />/>
    </div>
    <!--date de adhésion-->
    <div class="row">
        <label>Année de adhésion:</label>
        <select name="adhesion" class="tiny">
            <fmt:formatDate value="${ person.adhesion }" pattern="yyyy" var = "byear"/>
            <c:forEach items="${years}" var="year">
                <option <c:out value = "${byear == year?'selected':''}"/>>
                    <c:out value="${year}"/></option>
            </c:forEach>
        </select>
    </div>    
    <p class="${empty erreurs ? 'succes' : 'erreur'}">${message}</p>

</div>    
    

<c:if test="${googlemap!=null}">
    <div class="info">

        <h1>Géographie</h1>
        <img src="${googlemap}" alt="Pas d'image disponible"/>
        <!--remarques-->

    </div>
</c:if>
<div class="buttons">
    <c:if test="${empty lastAction}">
        <input type="submit" name="action" value="Nouveau" class="button"/>
        <input type="submit" name="action" value="Supprimer" class="button"/>
        <input type="submit" name="action" value="Changer" class="button"/>
    </c:if>
    <c:if test="${not empty lastAction}">
        <input type="submit" name="action" value="Annuler" class="button"/>
        <input type="submit" name="action" value="Confirmer" class="button"/>
    </c:if>

</div>
