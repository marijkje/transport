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
    
    private String sqlError;
    private Connection connexion;
    private Statement statement;
    
    public BaseRW()
    {
        sqlError = "";
        this.base = "transport";
        this.user = "mael1";
        this.password = "mael1tr";
        this.url = "jdbc:mysql://localhost:3306/" 
                + this.base 
                + "?user=" + this.user 
                + "&password=" + this.password;
        try 
        {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) 
        {
            sqlError = "sql driver not found";
        }    
    }

    public Statement connect()
    {
        try {
            connexion = (Connection) DriverManager.getConnection( url );
        } catch ( SQLException e ) 
        {
            sqlError = "Could not connect to MySQL";
            return null;
        }
        try {
            statement = (Statement) connexion.createStatement();
        } catch ( SQLException e ) 
        {
            sqlError = "Could not create sql statement";
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
    public String error()
    {
    return  sqlError;
    }
    
}
