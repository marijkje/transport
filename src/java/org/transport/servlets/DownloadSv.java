/* 
 * Copyright (C) 2017 Mael
 */
package org.transport.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.transport.format.DownloadFormat;

/**
 *
 * @author Mael
 */
public class DownloadSv extends HttpServlet {

    /**
     *  Method doGet called by menu.jsp 
     *  Confirms userrole 'admin' needed to access download.jsp
     *  Clears session message Forwards to download.jsp
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
            DownloadFormat form = new DownloadFormat(request, response);
            form.info();
            this.getServletContext().getRequestDispatcher( "/JSP/admin/download.jsp" ).forward( request, response );
        }
    }

    /**
     *  Method doPost called by download.jsp 'action' 
     *  Calls DownloadForm class to manipulate persons addresses
     *  Forwards to download.jsp
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
            DownloadFormat form = new DownloadFormat(request, response);
            form.action();
            this.getServletContext().getRequestDispatcher( "/JSP/admin/download.jsp" ).forward( request, response );
        }
    }
}
