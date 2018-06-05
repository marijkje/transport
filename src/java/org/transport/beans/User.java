/* 
 * Copyright (C) 2017 Mael
 */
package org.transport.beans;

import java.io.Serializable;

/**
 *  Bean containing users information
 * 
 * @author Mael
 */
public class User implements Serializable
{
    private String nom;
    private String motdepasse;
    private String role;

    public String getNom()
    {
        return this.nom;
    }

    public String getMotdepasse()
    {
        return this.motdepasse;
    }

    public String getRole()
    {
        return this.role;
    }

    public void setNom(String nom)
    {
        this.nom = nom;
    }

    public void setMotdepasse(String motdepasse)
    {
        this.motdepasse = motdepasse;
    }
    
    public void setRole(String role)
    {
        this.role = role;
    }

/**
 * 
 * return role and name as string user.xml file compatible 
 */
    @Override
    public String toString() {
        return this.role + " " + this.nom;
    }

}
