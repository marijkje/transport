/* 
 * Copyright (C) 2017 Mael
 */
package org.transport.beans;

import java.io.Serializable;
import java.util.Date;
import java.util.Locale;
import org.joda.time.DateTime;

/**
 *  sets and gets all values relative to a journey
 * 
 * @author Mael
 */
public class Drive implements Serializable
{
    public static final String MONTHS = "janvier,février,mars,avril,mai,juin,juillet,août,septembre,octobre,novembre,decembre";
    private Person driver;
    private Person client;
    private Person client1;
    private Person client2;
    private Person client3;
    private Date depart;
    private Date retour;
    private String adresseDepart;
    private String codeDepart;
    private String villeDepart;
    private String adresseArrivee;
    private String codeArrivee;
    private String villeArrivee;
    private int flexHeure;
    private int flexDate;
    private int places;
    private int kilometres;
    private String motif;

    public Drive()
    {
        places = 1;
    }
    
    
    public void setDriver(Person person)
    {
        if (person == null) person = new Person();
        this.driver = person;
    }

    public void setClient(Person person)
    {
        if (person == null) person = new Person();
        this.client = person;
    }

    public void setClient1(Person person)
    {
        if (person == null) person = new Person();
        this.client1 = person;
    }

    public void setClient2(Person person)
    {
        if (person == null) person = new Person();
        this.client2 = person;
    }

    public void setClient3(Person person)
    {
        if (person == null) person = new Person();
        this.client3 = person;
    }

    public void setDepart(Date depart)
    {
        this.depart = depart;
    }

    public void setRetour(Date retour)
    {
        this.retour = retour;
    }

    public void setAdresseDepart(String adresse)
    {
        this.adresseDepart = adresse;
    }

    public void setCodeDepart(String code)
    {
        this.codeDepart = code;
    }

    public void setVilleDepart(String ville)
    {
        this.villeDepart = ville;
    }

    public void setAdresseArrivee(String adresse)
    {
        this.adresseArrivee = adresse;
    }

    public void setCodeArrivee(String code)
    {
        this.codeArrivee = code;
    }

    public void setVilleArrivee(String ville)
    {
        this.villeArrivee = ville;
    }

    public void setMotif(String motif)
    {
        this.motif = motif;
    }

    public void setFlexHeure(int heure)
    {
        this.flexHeure = heure;
    }

    public void setFlexDate(int date)
    {
        this.flexDate = date;
    }

    public void setPlaces(int places)
    {
        if (places == 0) places = 1;
        this.places = places;
    }

    public void setKilometres(int km)
    {
        this.kilometres = km;
    }

    public Person getDriver()
    {
        return driver;
    }

    public Person getClient()
    {
        return client;
    }

    public Person getClient1()
    {
        return client1;
    }

    public Person getClient2()
    {
        return client2;
    }

    public Person getClient3()
    {
        return client3;
    }

    public Date getDepart()
    {
        return depart;
    }

    public Date getRetour()
    {
        return retour;
    }

    public String getAdresseDepart()
    {
        return adresseDepart;
    }

    public String getCodeDepart()
    {
        return codeDepart;
    }

    public String getVilleDepart()
    {
        return villeDepart;
    }

    public String getAdresseArrivee()
    {
        return adresseArrivee;
    }

    public String getCodeArrivee()
    {
        return codeArrivee;
    }

    public String getVilleArrivee()
    {
        return villeArrivee;
    }

    public String getMotif()
    {
        return motif;
    }

    public int getFlexHeure()
    {
        return flexHeure;
    }

    public int getFlexDate()
    {
        return flexDate;
    }

    public int getPlaces()
    {
        return places;
    }

    public int getKilometres()
    {
        return kilometres;
    }

   /**
     * 
     * @return name and last name of person and the departure date
     */
    @Override
    public String toString() {
        if (this.driver.getNom() != null) return this.driver + " " + this.getDateDepart();
        else return this.client + " " + this.getDateDepart();
    }
    
   /**
     * 
     * @return departure date as String
     */
    public String getDateDepart()
    {
        DateTime dt = new DateTime(depart);
        String day = dt.dayOfWeek().getAsText(Locale.FRENCH);
        String month = dt.monthOfYear().getAsText(Locale.FRENCH);
        return "le " + day + dt.toString(" dd ") + month + " à " + dt.toString("HH:mm");
    }

   /**
     * 
     * @return return date as String
     */
    public String getDateRetour()
    {
        DateTime dt = new DateTime(retour);
        String day = dt.dayOfWeek().getAsText(Locale.FRENCH);
        String month = dt.monthOfYear().getAsText(Locale.FRENCH);
        return "le " + day + dt.toString(" dd ") + month + " à " + dt.toString("HH:mm");
    }

   /**
     * 
     * @return departure address as google compatible address
     */
    public String googleDepart()
    {
        String str = this.adresseDepart+ "+" + this.villeDepart;
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
        
   /**
     * 
     * @return destination address as google compatible address
     */
    private String googleDestination()
    {
        String str = this.adresseArrivee+ "+" + this.villeArrivee;
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

    /**
     * Prepare the request for calculation of total distance, 
     * from departure address of the driver to destination and back
     * passing thru clients address or not (if no return journey) 
     * @param person - to know the persons address
     * @param driveInfo - to know if the person is a driver or a client
     * @return addresses String for google 'directions' request
     */
    
    public String googleAddresses(Person person, String driveInfo)
    {
        String addresses;
        if (driveInfo.equals("appeal") ) 
        {
            addresses = "origin=" + person.googleAddress() 
                    + "&destination=" + person.googleAddress() 
                    + "&waypoints=" + this.googleDepart() + "|" + this.googleDestination();
            if (this.retour != null) addresses += "|" + this.googleDepart();
        }
        else
        {
            addresses = "origin=" + this.googleDepart() 
                    + "&destination=" + this.googleDepart() 
                    + "&waypoints=" + person.googleAddress() + "|" + this.googleDestination();
            if (this.retour != null) addresses += "|" + person.googleAddress();
        }
        return addresses;
    }
}
