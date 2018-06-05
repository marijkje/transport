/* 
 * Copyright (C) 2017 Mael
 */
package org.transport.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.transport.format.SendFormat;

/**
 *
 * @author Mael
 */
public class SendSv extends HttpServlet
{
    private SendFormat send;
    /**
     *  Method doGet called by drive.jsp action = 'Contacter'
     *  Prepares mail and adresses to send mail to
     *  Forwards to send.jsp
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
            send = new SendFormat(request);
            send.info();
            this.getServletContext().getRequestDispatcher( "/JSP/user/send.jsp" ).forward( request, response );
        }
    }

    /**
     *  Method doPost called by send.jsp action = "Envoyer"
     *  retrieves session email text and list of adresses
     *  Forwards to send.jsp or drive.jsp
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
            send = new SendFormat(request);
            this.getServletContext().getRequestDispatcher( send.action() ).forward( request, response );
        }
    }

}
