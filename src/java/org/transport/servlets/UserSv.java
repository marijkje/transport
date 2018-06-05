/* 
 * Copyright (C) 2017 Mael
 */
package org.transport.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.transport.format.UserFormat;

/**
 *
 * @author Mael
 */
public class UserSv extends HttpServlet
{
    
    /**
     *  Method doGet called by menu.jsp 'act'='Utilisateur'
     *  Confirms userrole 'admin' needed to access user.jsp
     *  Calls Admin class method readAll to populate select list of users from 'users.xml' file
     *  
     *  @param request
     *  @param response
     *  @throws javax.servlet.ServletException
     *  @throws java.io.IOException
     */
 
    @Override
    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException 
    {
        if (request.getSession(false) == null) 
        {
            request.getSession().setAttribute("expired", true);
            request.getRequestDispatcher( "index.jsp" ).forward( request, response );
        }        
        else 
        {
            request.getSession().setAttribute("expired", false);
            UserFormat form = new UserFormat(request);
            form.info();
            this.getServletContext().getRequestDispatcher( "/JSP/admin/users.jsp" ).forward( request, response );
        }
    }
    
    /**
     *  Method doPost called by user.jsp 'action' or 'select change'
     *  Calls Userform class to manipulate users.xml file (add or delete users)
     *  Forwards to address.jsp
     *
     *  @param request servlet request
     *  @param response servlet response
     *  @throws ServletException if a servlet-specific error occurs
     *  @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        if (request.getSession(false) == null) {
            request.getSession().setAttribute("expired", true);
            request.getRequestDispatcher( "index.jsp" ).forward( request, response );
        }        
        else 
        {
            request.getSession().setAttribute("expired", false);
            UserFormat form = new UserFormat(request);
            form.action();
            this.getServletContext().getRequestDispatcher("/JSP/admin/users.jsp").forward( request, response );
        }
    }

    
}