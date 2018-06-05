/* 
 * Copyright (C) 2017 Mael
 */
package org.transport.filters;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.transport.readwrite.AdminRW;

/**
 *  Passes in login.jsp retrieved user information to User class 
 *  allowed method to compare userinfo with registered 'user' users
 *  Forwards to demanded pages or login.jsp if user not allowed
 * 
 * @author Mael
 */
public class UserFilter implements Filter 
{
    public static final String ATT_SESSION_USER = "sessionUser";
    
    @Override
    public void init( FilterConfig config ) throws ServletException 
    {
    }

    @Override
    public void doFilter( ServletRequest req, ServletResponse resp, FilterChain chain ) throws IOException,
            ServletException 
    {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpSession session = request.getSession();
        
        if (request.getParameter("act") != null) session.setAttribute("act", request.getParameter("act"));

        String name = (String) session.getAttribute( "username" );
        String pass = (String) session.getAttribute( "userpass" );
        
        if (name!=null)
        {
            try {
                AdminRW adm = new AdminRW();
                if (adm.isAllowed(name, pass, "user") || adm.isAllowed(name, pass, "admin"))
                {
                    chain.doFilter( req, resp );
                    return;
                }
            }
            catch (Exception ex) {
                Logger.getLogger(UserFilter.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        request.getRequestDispatcher( "/JSP/login.jsp" ).forward( req, resp ); 
    }


    @Override
    public void destroy() {
    }
}
