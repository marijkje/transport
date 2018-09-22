/* 
 * Copyright (C) 2017 Mael
 */
package org.transport.readwrite;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.transport.beans.Drive;
import org.transport.beans.Person;

/**
 *  Performs read/write actions with client and driver databases
 *  Called by DoneForm
 *  
 * @author Mael
 */
public class DoneRW
{
    private final String table;
    private final BaseRW db;
    
    public DoneRW()
    {
        this.db = new BaseRW();
        this.table = "done";
    }

    public String delete(Drive drive)
    {
        String message = "";
       
        Person driver = drive.getDriver();
        Person client = drive.getClient();
        Statement statement;
        
        try {
            statement = db.connect();
            int nb = statement.executeUpdate("DELETE FROM "  + this.table 
                    + " WHERE nomchauffeur='"   + drive.getDriver().getNom() 
                    + "' AND prenomchauffeur='" + drive.getDriver().getPrenom() 
                    + "' AND depart='"          + drive.getDepart() + "'");

            if (nb == 0) message = " Le voyage de " + drive.toString() + " n'a pas été enlevé. ";
            else message = " Le voyage de " + drive.toString() + " a été enlevé(e). ";
        } catch ( SQLException e ) {
            message += "sqlexception : " + e;
        } 
        db.close();
        message += this.decrement(driver, client);
        return message;
    }

    private String driveDelete(Drive drive, String info)
    {
        String message;
       
        DriveRW drv = new DriveRW(info);
        message = drv.delete(drive);
        return message;
    }

    private String increment(Person driver, Person client)
    {
       PersonRW addr;
       String message;
       
       addr = new PersonRW("drivers"); 
       message = addr.increment(driver);
       addr = new PersonRW("clients"); 
       message += addr.increment(client);
       return message;
    }
    
    private String decrement(Person driver, Person client)
    {
       PersonRW addr;
       String message;
       
       addr = new PersonRW("drivers"); 
       message = addr.decrement(driver);
       addr = new PersonRW("clients"); 
       message += addr.decrement(client);
       return message;
    }
    
    public String write(Drive drive, Person pers, String info)
    {
        Person driver, client;
        String message = "";
        Person person;
        if (info.equals("appeal"))
            person = drive.getClient();
        else
            person = drive.getDriver();
        if (info.equals("appeal"))
        {
            driver = pers;
            client = person;
        }
        else
        {
            driver = person;
            client = pers;
        }
    
    
        String sel = " (nomchauffeur, prenomchauffeur, nomclient, prenomclient, adressedepart, adressearrivee, "
                    + "codedepart, codearrivee, "
                    + "communedepart, communearrivee, depart, ";
        if (drive.getRetour()!= null) sel += "retour, ";
        sel += "places, kilometres, motif) ";
        String val = "( '"  + driver.getNom() + "', '"
                            + driver.getPrenom() + "', '"
                            + client.getNom() + "', '"
                            + client.getPrenom() + "', '"
                            + drive.getAdresseDepart() + "', '"
                            + drive.getAdresseArrivee() + "', '"
                            + drive.getCodeDepart() + "', '"
                            + drive.getCodeArrivee() + "', '"
                            + drive.getVilleDepart() + "', '"
                            + drive.getVilleArrivee() + "', '"
                            + drive.getDepart() + "', '";
        if (drive.getRetour()!= null) val += drive.getRetour() + "', '";
        val += drive.getPlaces() + "', '"
                            + drive.getKilometres() + "', '"
                            + drive.getMotif() + "' )";

        Statement statement;
        
        try {
            statement = db.connect();
            statement.executeUpdate("INSERT INTO " + this.table + sel + " VALUES " + val + "");
            message += "Le transport de " + driver.getPrenom() + " " + driver.getNom() + " a été enregistré(e) ";
        } catch ( SQLException e ) {
            final String code = e.getSQLState();
            if (code.equals("23505")) message += "Ce voyage était déjà enrégistré";
            else message += "Une erreur d'écriture s'est produite";
        } 
        db.close();
        message += this.driveDelete(drive, info);
        message += this.increment(driver, client);
        return message;
    }
    
    public List<Drive> readAll()
    {
        int cnt = 0;
        List<Drive> all = new ArrayList<>();
        PersonRW addr;

        Statement statement;
        ResultSet resultat = null;
        
        try {

            statement = db.connect();
            resultat = (ResultSet) statement.executeQuery("SELECT * FROM " + this.table + " ORDER BY depart, nomchauffeur, prenomchauffeur" );
            
            while (resultat.next())
            {
                Drive drive = new Drive();
                Person driver = new Person();
                Person client = new Person();
                driver.setNom(resultat.getString("nomchauffeur"));
                driver.setPrenom(resultat.getString("prenomchauffeur"));
                client.setNom(resultat.getString("nomclient"));
                client.setPrenom(resultat.getString("prenomclient"));
                drive.setAdresseDepart(resultat.getString("adresseDepart"));
                drive.setAdresseArrivee(resultat.getString("adresseArrivee"));
                drive.setCodeDepart(resultat.getString("codeDepart"));
                drive.setCodeArrivee(resultat.getString("codeArrivee"));
                drive.setVilleDepart(resultat.getString("communeDepart"));
                drive.setVilleArrivee(resultat.getString("communeArrivee"));
                drive.setMotif(resultat.getString("motif"));
                drive.setDepart(resultat.getTimestamp("depart"));
                drive.setRetour(resultat.getTimestamp("retour"));
                drive.setPlaces(resultat.getInt("places"));
                drive.setKilometres(resultat.getInt("kilometres"));
                addr = new PersonRW("drivers");
                drive.setDriver(addr.read(driver));
                addr = new PersonRW("clients");
                drive.setClient(addr.read(client));
                all.add(drive);
                cnt++;
            }
        } catch ( SQLException e ) {


        } finally {
        if ( resultat != null )
            try {
                resultat.close();

                } catch ( SQLException ignore ) {
            }
        }
        db.close();
        return all;
    }


    /**
     *  With name, last name and departure time the drives information is retrieved from database
     *  
     * @param drive
     * @return 
     */
    
    public Drive read(Drive drive)
    {
        if (drive == null) return null;
        Person person = drive.getDriver();
        if (person == null) return null;
        Statement statement;
        ResultSet resultat = null;

        PersonRW addr;

        
        try {

            statement = db.connect();
            resultat = (ResultSet) statement.executeQuery("SELECT * FROM " + this.table + 
                                                           " WHERE nomchauffeur='" + person.getNom() + 
                                                           "' AND prenomchauffeur='" + person.getPrenom() + 
                                                           "' AND depart='" + drive.getDepart() + "'" );
            resultat.next();
            Person driver = new Person();
            Person client = new Person();
            driver.setNom(resultat.getString("nomchauffeur"));
            driver.setPrenom(resultat.getString("prenomchauffeur"));
            client.setNom(resultat.getString("nomclient"));
            client.setPrenom(resultat.getString("prenomclient"));
            drive.setAdresseDepart(resultat.getString("adresseDepart"));
            drive.setAdresseArrivee(resultat.getString("adresseArrivee"));
            drive.setCodeDepart(resultat.getString("codeDepart"));
            drive.setCodeArrivee(resultat.getString("codeArrivee"));
            drive.setVilleDepart(resultat.getString("communeDepart"));
            drive.setVilleArrivee(resultat.getString("communeArrivee"));
            drive.setMotif(resultat.getString("motif"));
            drive.setDepart(resultat.getTimestamp("depart"));
            drive.setRetour(resultat.getTimestamp("retour"));
            drive.setPlaces(resultat.getInt("places"));
            drive.setKilometres(resultat.getInt("kilometres"));
            addr = new PersonRW("drivers");
            drive.setDriver(addr.read(driver));
            addr = new PersonRW("clients");
            drive.setClient(addr.read(client));
            
        } catch ( SQLException e ) {

 
        } finally {
        if ( resultat != null )
            try {
                resultat.close();

                } catch ( SQLException ignore ) {
            }
        }
        db.close();
        return drive;
    }


    /**
     *  Write data in CSV file
     *  
     * @param response
     * @param path
     * @return 
     */
    public String writeCSV(HttpServletResponse response, String path)
    {
        int cnt =0;
        Statement statement;
        ResultSet resultat = null;

        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition","attachment;filename=" + path);
        ServletOutputStream out;
        try 
        {
            BufferedWriter bw;
            out = response.getOutputStream();
            try (OutputStreamWriter writer = new OutputStreamWriter(out, "UTF-8")) {
                bw = new BufferedWriter(writer);
                String line = ""; 
                line += "nomchauffeur;prenomchauffeur;nomclient;prenomclient;adressedepart;adressearrivee;"
                        + "codedepart;codearrivee;"
                        + "communedepart;communearrivee;depart;retour;places;kilometres;motif"; 
                bw.write(line);
                bw.newLine();
                // and the data
                try {
                    
                    statement = db.connect();
                    resultat = (ResultSet) statement.executeQuery("SELECT * FROM " + this.table + " ORDER BY depart, nomchauffeur, prenomchauffeur" );
                    
                    while (resultat.next())
                    {
                        
                        line = resultat.getString("nomchauffeur") + ";" +
                            resultat.getString("prenomchauffeur") + ";" +
                            resultat.getString("nomclient") + ";" +
                            resultat.getString("prenomclient") + ";" +
                            resultat.getString("adresseDepart") + ";" +
                            resultat.getString("adresseArrivee") + ";" +
                            resultat.getString("codeDepart") + ";" +
                            resultat.getString("codeArrivee") + ";" +
                            resultat.getString("communeDepart") + ";" +
                            resultat.getString("communeArrivee") + ";" +
                            resultat.getTimestamp("depart") + ";" +
                            resultat.getTimestamp("retour") + ";" +
                            resultat.getInt("places") + ";" +
                            resultat.getInt("kilometres") + ";" +
                            resultat.getString("motif");
                        bw.write(line);
                        bw.newLine();
                        bw.flush();
                        cnt++;
                    }
                } catch ( SQLException e ) {


                } finally {
                if ( resultat != null )
                    try {
                        resultat.close();

                        } catch ( SQLException ignore ) {
                    }
                }
                db.close();
                out.close();
                bw.close();
            }
        }
        catch(IOException e) 
        {
            Logger.getLogger(PersonRW.class.getName()).log(Level.SEVERE, null, e);
            return "Le fichier n'a pas été trouvé";
        }
        
        return "Clickez ici pour les transports effectués : "+ cnt;
    }
    
}