/* 
 * Copyright (C) 2017 Mael
 */
package org.transport.readwrite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 
 * @author Mael
 */
public class BaseRW {
    private final String base;
    private final String url;
    private final String user;
    private final String password;
    
    private Connection connexion;
    private Statement statement;
    
    public BaseRW()
    {
        this.base = "transport";
        this.user = "mael";
        this.password = "mael";
        this.url = "jdbc:postgresql://localhost:5432/" 
                + this.base 
                + "?user=" + this.user 
                + "&password=" + this.password 
                + "&create=true";
        try 
        {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) 
        {
            throw new IllegalStateException("Cannot find the driver in the classpath!", e);
        }    
    }

    public Statement connect()
    {
        try {
            connexion = (Connection) DriverManager.getConnection( url );
            statement = (Statement) connexion.createStatement();
        } catch ( SQLException e ) 
        {
            return null;
        }
        return statement;
    }
    
    public void close()
    {
        if ( statement != null )
            try {
                statement.close();

                } catch ( SQLException ignore ) {
            }
        
        if ( connexion != null )
            try {
                connexion.close();

                } catch ( SQLException ignore ) {
            }

    }
    
}
