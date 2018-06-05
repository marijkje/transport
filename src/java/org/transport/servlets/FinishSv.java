/* 
 * Copyright (C) 2017 Mael
 */
package org.transport.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.transport.format.FinishFormat;

/**
 *
 * @author Mael
 */
public class FinishSv extends HttpServlet
{
    private FinishFormat finish;
    /**
     *  Method doGet called by menu.jsp act='Chercher'
     *  Confirms userrole 'user' needed to access drive.jsp
     *  Get Client Address object the clientlist for the drive.jsp select list
     *  Clears session message Forwards to drive.jsp
     *  
     *  @param request
     *  @param response
     *  @throws javax.servlet.ServletException
     *  @throws java.io.IOException
     */
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        if (request.getSession(false) == null) 
        {
            request.getSession().setAttribute("expired", true);
            request.getRequestDispatcher( "index.jsp" ).forward( request, response );
        }        
        else 
        {
            request.getSession().setAttribute("expired", false);
            finish = new FinishFormat(request);
            finish.info();
            this.getServletContext().getRequestDispatcher( "/JSP/user/finish.jsp" ).forward( request, response );
        }
    }

    /**
     *  Method doPost called by drive.jsp 'action' or 'select change'
     *  retrieves session 'clientlist' and its' selection
     *  Calls Driveform class to find drivers for selected client
     *  Forwards to drive.jsp
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
            finish = new FinishFormat(request);
            finish.action();
            this.getServletContext().getRequestDispatcher( "/JSP/user/finish.jsp" ).forward( request, response );
        }
    }
}
