/* 
 * Copyright (C) 2017 Mael
 */
package org.transport.format;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.transport.beans.User;
import org.transport.readwrite.AdminRW;
import org.transport.servlets.UserSv;

/**
 *  Processes data retrieved from PostUser servlet
 *  Communicates back to Servlet and/or to read/write class User
 *  In order to make changes in users or administrators list 
 * 
 *  @author Mael
 */
public class UserFormat
{
    private final String action;
    private final Map<String, String> errors;
    private final HttpSession session;
    private final HttpServletRequest request;
    
    public UserFormat(HttpServletRequest request)
    {
        this.request = request;
        this.session = request.getSession();
        this.action = request.getParameter("action");
        errors = new HashMap<>();
        request.setAttribute("message", "");
    }

    public Map<String, String> getErrors() 
    {
        return errors;
    }
    
    public void info()
    {
        try
        {
            request.setAttribute("message", "");
            AdminRW adm = new AdminRW();
            session.setAttribute("userList", adm.readAll());
            session.setAttribute("user", new User());
        }
        catch (Exception ex)
        {
            Logger.getLogger(UserSv.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    

    /**
     *  performs 'action' demanded by user
     *  No action ('action' == null) means that an address was selected in the select list
     *  'Clean'empties inputs of user.jsp
     *  'Confirmer' executes the last action demanded (lastAction)
     *  'Supprimer' et 'Ajouter' deletes or adds an address after confirmation
     */
    
    public void action()
    {
        try
        {
            User user ;
            AdminRW admin = new AdminRW();
            
            if (this.action == null) 
            {
                int offset =  Integer.parseInt(request.getParameter("search"));
                List<User> users = (List<User>) session.getAttribute("userList");
                user = users.get(offset);
            }
            else
                switch( this.action )
                {
                    case "Vider": 
                        session.setAttribute("lastAction", "");
                        request.setAttribute("message", "");
                        user = new User();
                        break;
                    case "Confirmer":
                        user = (User) session.getAttribute("user");
                        if ( errors.isEmpty())
                        {
                            if (this.action.equals("Confirmer") && session.getAttribute("lastAction") != null)
                            {
                                String message = "";
                                switch (session.getAttribute("lastAction").toString())
                                {
                                    case "Supprimer":
                                        message = admin.delete(user);
                                        break;
                                    case "Nouveau":
                                        message = admin.write(user);
                                        break;
                                    case "Changer":
                                        admin.delete(user);
                                        message = admin.write(user);
                                        break;
                                }
                                request.setAttribute( "message", message);
                            }
                        }
                        break;
                    default:    
                        user = getInfo();
                        String message = "cliquez sur 'Confirmer' pour " + this.action;
                        session.setAttribute( "lastAction", this.action);
                        request.setAttribute( "message", message);
                }
            session.setAttribute("userList", admin.readAll());
            session.setAttribute( "user", user );
            request.setAttribute("erreurs", errors);
        }
        catch (Exception ex)
        {
            Logger.getLogger(UserFormat.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     *  getInfo() Validates all User info
     *  
     *  @return completed User bean
     */
    private User getInfo()
    {
        User user = new User();
        String str = request.getParameter("nom");
        validationNom(str, user);
        str = request.getParameter("motdepasse");
        validationMotdepasse(str, user);
        str = request.getParameter("role");
        validationRole(str, user);
        return user;
    }
    
    
    private void validationNom(String str, User user)
    {
        String nom = alphaNumerique(str);
        if (nom != null)
        {
            user.setNom(nom);
            return;
        }
        errors.put("nom", "le nom n'est pas valide.");
    }
    
    private void validationMotdepasse(String str, User user)
    {
        String mdp = alphaNumerique(str);
        if (mdp != null)
        {
            user.setMotdepasse(mdp);
            return;
        }   
        errors.put("motdepasse", "le mot de passe n'est pas valide.");
    }

    private void validationRole(String str, User user)
    {
        String role = alphaNumerique(str);
        if (role != null)
            if (role.contains("admin") || role.contains("user")) 
            {    
                user.setRole(role);
                return;
            }
        errors.put("role", "le role n'est pas valide.");
    }
    
    private String alphaNumerique(String str)
    {
        String string = str.trim();
        if (string.matches("[a-zA-Z0-9@_., ]+") && string.length() > 3)
        {
            return string;
        }
        return null;
    }
    
}
