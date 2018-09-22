/* 
 * Copyright (C) 2017 Mael
 */
package org.transport.beans;

import java.io.Serializable;
import java.util.Date;

/**
 *  Bean containing persons information
 * 
 *  @author Mael
 */
public class Person implements Serializable
{
    private String nom;
    private String prenom;
    private String civilite;
    private String adresse;
    private String code;
    private String ville;
    private String tel;
    private String email;
    private String remarques;
    private int nombre;
    private int places;
    private boolean charte;
    private String contact;
    private String contacttel;
    private Date naissance;
    private Date adhesion;
    
    private Dispo dispo;
    
    public String getNom()
    {
        return nom;
    }

    public String getPrenom()
    {
        return prenom;
    }

    public String getCivilite()
    {
        return civilite;
    }

    public String getAdresse()
    {
        return adresse;
    }

    public String getCode()
    {
        return code;
    }

    public String getVille()
    {
        return ville;
    }

    public String getTel()
    {
        return tel;
    }

    public String getEmail()
    {
        return email;
    }

    public Date getNaissance()
    {
        return naissance;
    }
    
    public Date getAdhesion()
    {
        return adhesion;
    }
    
    public String getRemarques()
    {
        return remarques;
    }
    
    public int getNombre()
    {
        return nombre;
    }

    public int getPlaces()
    {
        return places;
    }

    public boolean getCharte()
    {
        return charte;
    }
    
    public String getContact()
    {
        return contact;
    }
    
    public String getContacttel()
    {
        return contacttel;
    }
    
    public Dispo getDispo()
    {
        return dispo;
    }
    
    public void setNom(String nom)
    {
        this.nom = nom;
    }

    public void setPrenom(String prenom)
    {
        this.prenom = prenom;
    }
    
    public void setCivilite(String civilite)
    {
        this.civilite = civilite;
    }
    
    public void setAdresse(String adresse)
    {
        this.adresse = adresse;
    }
    
    public void setCode(String code)
    {
        this.code = code;
    }
    
    public void setVille(String ville)
    {
        this.ville = ville;
    }
    
    public void setTel(String tel)
    {
        this.tel = tel;
    }
    
    public void setEmail(String email)
    {
        this.email = email;
    }
    
    public void setNaissance(Date date)
    {
            this.naissance = date;
    }
    
    public void setAdhesion(Date date)
    {
            this.adhesion = date;
    }
    
    public void setRemarques(String remarques)
    {
        this.remarques = remarques;
    }
    
    public void setNombre(int nombre)
    {
        this.nombre = nombre;
    }
    
    public void setPlaces(int places)
    {
        this.places = places;
    }
    
    public void setCharte(boolean charte)
    {
        this.charte = charte;
    }
    
    public void setContact(String contact)
    {
        this.contact = contact;
    }
    
    public void setContacttel(String contacttel)
    {
        this.contacttel = contacttel;
    }
    
    public void setDispo(Dispo dispo)
    {
        this.dispo = dispo;
    }
    /**
     * 
     * @return name and last name of person 
     */
    @Override
    public String toString() {
        return this.civilite + " " + this.prenom + " " + this.nom;
    }

    /**
     * 
     * @return google request compatible address 
     */
    public String googleAddress()
    {
        String str = this.adresse+ "+" + this.ville;
        str = str.replace(' ', '+');
        str = str.replace('é', 'e');
        str = str.replace('è', 'e');
        str = str.replace('ê', 'e');
        str = str.replace('ë', 'e');
        str = str.replace('à', 'a');
        str = str.replace('ù', 'u');
        str = str.replace('ç', 'c');
        return str;
    }
        
    
}
