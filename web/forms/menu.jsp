<%@ page pageEncoding="UTF-8" %>
<div class="form">
    <div class="menu-block">
        <form style="display:inline-block;" method="get" action="adresse">
            <input class="menu" name="act" type="submit" value="Demandeur" />
            <input class="menu" name="act" type="submit" value="Chauffeur" />
        </form>

        <form style="display:inline-block;" method="get" action="drive">
            <input class="menu" type="submit" name="act" value="Demander" />
            <input class="menu" type="submit" name="act" value="Proposer" />
        </form>

        <form style="display:inline-block;" method="get" action="finish">
            <input class="menu" type="submit" name="act" value="Stocker" />
        </form>
        <form style="display:inline-block;" method="get" action="user">
            <input class="special" type="submit" name="act" value="U" />
        </form>
        <form style="display:inline-block;" method="get" action="download">
            <input class="special" type="submit" name="act" value="T" />
        </form>
        <div class="user"> Utilisateur : <c:out value="${username}"/></div>
    </div>
    
    
</div>


<div id="logo"> </div>
<div class="title">
<h1>Transport solidaire</h1>

<h2>DE L'EYRIEUX AUX SERRES</h2>
</div>





