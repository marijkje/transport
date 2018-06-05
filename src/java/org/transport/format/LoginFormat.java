/* 
 * Copyright (C) 2017 Mael
 */
package org.transport.format;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.transport.beans.User;

/**
 *  Called from LoginSV Servlet to execute actions demanded by posted form of login.jsp
 * 
 * @author Mael
 */
public class LoginFormat
{
    private final Map<String, String> errors;
    HttpServletRequest request;
    
    public LoginFormat(HttpServletRequest request)
    {
        this.request = request;
        errors = new HashMap<>();
    }

    public Map<String, String> getErrors() 
    {
        return errors;
    }
    
    /**
     *  If the user filled in the form correctly its stocked in the session userinfo
     *  used by the request filter 
     *  Data is prepared for the demanded page and the url of this page is returned
     * 
     * @return 
     */
    public String connectUser()
    {
        User user = new User();
        
        String str = request.getParameter("nom");
        validationNom(str, user);
        str = request.getParameter("motdepasse");
        validationMotdepasse(str, user);
        HttpSession session = request.getSession();

        if ( errors.isEmpty() && session.getAttribute("act") != null) 
        {
            session.setAttribute( "username", user.getNom() );
            session.setAttribute( "userpass", user.getMotdepasse() );
            if (session.getAttribute("act") != null) str = session.getAttribute("act").toString();
            switch (str)
            {
                case "U":
                    return "/JSP/admin/users.jsp";
                case "T":
                    return "/JSP/admin/download.jsp";
                case "Stocker":
                    return "/JSP/user/finish.jsp";
                case "Demander":
                case "Proposer":
                    return "/JSP/user/drive.jsp";
                case "Demandeur":
                case "Chauffeur":
                    return "/JSP/user/address.jsp";
            }
        }
        session.setAttribute( "username", null );
        return "/JSP/login.jsp";
    }
    
    private void validationNom(String str, User user)
    {
        String nom = str.trim();
        if (nom.matches("[a-zA-Z0-9]+") && !nom.isEmpty()) user.setNom(nom);
        else  errors.put("nom", "le nom n'est pas valide.");
    }
    
    private void validationMotdepasse(String str, User user)
    {
        String mdp = str.trim();
        if (mdp.matches("[a-zA-Z0-9@_.]+") && mdp.length() > 3 )
        {
            user.setMotdepasse(mdp);
        } else  errors.put("motdepasse", "le mot de passe n'est pas valide.");
    }

}
