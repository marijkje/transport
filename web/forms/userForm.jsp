<%@page contentType="text/html" pageEncoding="UTF-8"%>


<div class="info">

    <h1>Utilisateur</h1>

    <div class="row">
        <label>
            <select name="search" onchange="this.form.submit()">
                <option value="Selectionnez un utilisateur" disabled selected style="display:none;"></option> 
                <c:forEach var="us" items="${userList}" varStatus="i">
                    <option value="<c:out value="${i.index}"/>" 
                            <c:out value="${us.nom == user.nom && us.role == user.role ? 'selected' : ''}"/>>
                        <c:out value="${us.nom}"/>
                        <c:out value="${us.role}"/>
                    </option>
                </c:forEach> 
            </select>
        </label>
    </div>
    <div class="row">
        <input type="text" id="nom" name="nom" value="<c:out value="${ user.nom }"/>" maxlength="45" placeholder="Nom d'utilisateur"/>
        <span class="erreur">${erreurs['nom']}</span>
    </div>

    <div class="row">
        <input type="password" id="motdepasse" name="motdepasse" value="<c:out value="${ user.motdepasse }"/>" maxlength="45" placeholder="Mot de passe"/>
        <span class="erreur">${erreurs['motdepasse']}</span>
    </div>

    <div class="row">
        <input type="text" id="role" name="role" value="<c:out value="${ user.role }"/>" maxlength="45" placeholder="Rôle ('admin' ou 'user')"/>
        <span class="erreur">${erreurs['role']}</span>
    </div>

    <input type="submit" name="action" value="Nouveau" class="button"/>
    <input type="submit" name="action" value="Supprimer" class="button"/>
    <input type="submit" name="action" value="Changer" class="button"/>
    <input type="submit" name="action" value="Vider" class="button"/>
    <input type="submit" name="action" value="Confirmer" class="button"/>
    <p class="${empty erreurs ? 'succes' : 'erreur'}">${message}</p>
</div>
