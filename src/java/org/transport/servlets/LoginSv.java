/* 
 * Copyright (C) 2017 Mael
 */
package org.transport.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.transport.format.LoginFormat;

/**
 *
 * @author Mael
 */
public class LoginSv extends HttpServlet
{
    
    /**
     *  Called by Filters to get user info ('admin' for address and user manipulation and 'user' for find action)
     * 
     *  @param request servlet request
     *  @param response servlet response
     *  @throws ServletException if a servlet-specific error occurs
     *  @throws IOException if an I/O error occurs
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
        }
        this.getServletContext().getRequestDispatcher( "/JSP/login.jsp" ).forward( request, response );
    }
    
    /**
     * retrieves user information from the login.jsp form
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
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
            LoginFormat form = new LoginFormat(request);
            String url = form.connectUser();
            request.setAttribute( "erreurs", form.getErrors() );
            this.getServletContext().getRequestDispatcher( url ).forward( request, response );
        }
    }

    
}
