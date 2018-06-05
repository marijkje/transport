/* 
 * Copyright (C) 2017 Mael
 */
package org.transport.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.transport.format.AddressFormat;

/**
 *
 * @author Mael
 */
public class AddressSv extends HttpServlet
{

    /**
     *  Method doGet called by menu.jsp 'act'='Client' or 'act'='Chauffeur'
     *  Confirms userrole 'user' needed to access address.jsp
     *  Clears session message Forwards to address.jsp
     *  Sets session 'personInfo' to 'clients' or 'drivers' to choose personList
     *  Gets 'act' Address object to retrieve the personlist for the address.jsp select list 
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
            AddressFormat form = new AddressFormat(request);
            form.info();
            this.getServletContext().getRequestDispatcher( "/JSP/user/address.jsp" ).forward( request, response );
        }
    }

    /**
     *  Method doPost called by address.jsp 'action' or 'select change'
     *  retrieves session 'personlist' and its' selection
     *  Calls Addressform class to manipulate persons addresses
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
        if (request.getSession(false) == null) 
        {
            request.getSession().setAttribute("expired", true);
            request.getRequestDispatcher( "index.jsp" ).forward( request, response );
        }        
        else 
        {
            request.getSession().setAttribute("expired", false);
            AddressFormat form = new AddressFormat(request);
            form.action();
            this.getServletContext().getRequestDispatcher( "/JSP/user/address.jsp" ).forward( request, response );
        }
    }

}
