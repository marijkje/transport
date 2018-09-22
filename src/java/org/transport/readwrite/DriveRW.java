/* 
 * Copyright (C) 2017 Mael
 */
package org.transport.readwrite;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
 *  Called by Addressform
 *  
 * @author Mael
 */
public class DriveRW
{
    private final String table;
    private final BaseRW db;
    
    public DriveRW(String driveInfo)
    {

        this.db = new BaseRW();
        this.table = driveInfo;
    }

    
    /**
     * If a person is deleted from the database, all his/her planned drives must be deleted too
     * 
     * @param person
     * @return 
     */
    public String deleteAll(Person person)
    {
        String message;
        
        String nom, prenom;
        if (table.equals("offer")) 
        {
            nom = "nomchauffeur";
            prenom = "prenomchauffeur";
        }
        else 
        {
            nom = "nomclient";
            prenom = "prenomclient";
        }
        
        Statement statement;
        
        try {
            statement = db.connect();
            statement.executeUpdate("DELETE FROM " + this.table + 
                    " WHERE " + nom + "='" + person.getNom() + "' AND " + prenom + "='" + person.getPrenom() + "'");
            
            message = "Les voyages de " + person.getPrenom() + " " + person.getNom() + " ont été enlevé(e)s";
        } catch ( SQLException e ) 
        {
            message = "Les voyages de " + person.getPrenom() + " " + person.getNom() + " n'ont pas été enlevé(e)s";
        } 
        db.close();
        return message;
    }
    
    
    public String delete(Drive drive)
    {
        String message;
        
        Person person;
        String nom, prenom;
        if (table.equals("offer")) 
        {
            person = drive.getDriver();
            nom = "nomchauffeur";
            prenom = "prenomchauffeur";
        }
        else 
        {
            person = drive.getClient();
            nom = "nomclient";
            prenom = "prenomclient";
        }
        
        Statement statement;
        
        try {
            statement = db.connect();
            statement.executeUpdate("DELETE FROM " + this.table + 
                    " WHERE " + nom + "='" + person.getNom() + "' AND " + prenom + "='" + person.getPrenom() + "' AND depart='" + drive.getDepart() + "'");
            
            message = "Le voyage de " + person.getPrenom() + " " + person.getNom() + " a été enlevé(e)";
        } catch ( SQLException e ) 
        {
            message = "Le voyage de " + person.getPrenom() + " " + person.getNom() + " n'a pas été enlevé(e)";
        } 
        db.close();
        return message;
    }

    
    public String write(Drive drive)
    {
        DateFormat sqlDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String message = "";
        String nom, prenom;
        
        this.delete(drive); // if drive exists already we change it
    
        Person person;
        if (table.equals("offer")) 
        {
            nom = "nomchauffeur";
            prenom = "prenomchauffeur";
            person = drive.getDriver();
        }
        else 
        {
            nom = "nomclient";
            prenom = "prenomclient";
            person = drive.getClient();
        }
    
        
        
        String sel = " (" + nom + ", " + prenom + ", adressedepart, adressearrivee, codedepart, codearrivee, "
                    + "communedepart, communearrivee, depart, ";
        if (drive.getRetour()!= null) sel += "retour, ";
        sel +=  "motif, flexheure, flexdate, places) ";
        String val = "( '"  + person.getNom() + "', '"
                            + person.getPrenom() + "', '"
                            + drive.getAdresseDepart() + "', '"
                            + drive.getAdresseArrivee() + "', '"
                            + drive.getCodeDepart() + "', '"
                            + drive.getCodeArrivee() + "', '"
                            + drive.getVilleDepart() + "', '"
                            + drive.getVilleArrivee() + "', '"
                            + sqlDateFormat.format(drive.getDepart()) + "', '";
        if (drive.getRetour()!= null) val += sqlDateFormat.format(drive.getRetour()) + "', '";
        val += drive.getMotif() + "', '"
                            + drive.getFlexHeure() + "', '"
                            + drive.getFlexDate() + "', '"
                            + drive.getPlaces() + "' )";

        Statement statement;
        
        try {
            statement = db.connect();
            statement.executeUpdate("INSERT INTO " + this.table + sel + " VALUES " + val + "");
            message += "La demande de " + person.getPrenom() + " " + person.getNom() + " a été enregistré(e): ";
        } catch ( SQLException e ) {
            final String code = e.getSQLState();
            if (code.equals("23505")) message += "Ce voyage était déjà enrégistré. ";
            else message += "Une erreur d'écriture s'est produite. " + e;
        } 
        db.close();
        return message;
    }
    
    public List<Drive> readAll()
    {
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
                
                drive.setMotif(resultat.getString("motif"));
                driver.setNom(resultat.getString("nomchauffeur"));
                driver.setPrenom(resultat.getString("prenomchauffeur"));
                addr = new PersonRW("drivers");
                drive.setDriver(addr.read(driver));
                client.setNom(resultat.getString("nomclient"));
                client.setPrenom(resultat.getString("prenomclient"));
                addr = new PersonRW("clients");
                drive.setClient(addr.read(client));
                drive.setAdresseDepart(resultat.getString("adressedepart"));
                drive.setAdresseArrivee(resultat.getString("adressearrivee"));
                drive.setCodeDepart(resultat.getString("codedepart"));
                drive.setCodeArrivee(resultat.getString("codearrivee"));
                drive.setVilleDepart(resultat.getString("communedepart"));
                drive.setVilleArrivee(resultat.getString("communearrivee"));
                drive.setDepart(resultat.getTimestamp("depart"));
                drive.setRetour(resultat.getTimestamp("retour"));
                drive.setFlexHeure(resultat.getInt("flexheure"));
                drive.setFlexDate(resultat.getInt("flexdate"));
                drive.setPlaces(resultat.getInt("places"));
                all.add(drive);
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
     *  With name and last name the drive information is retrieved from database
     *  
     * @param drive
     * @return 
    */
    public Drive read(Drive drive)
    {
        if (drive == null) return null;
        Person person;
        if (table.equals("offer")) person = drive.getDriver();
        else person = drive.getClient();

        if (person == null) return null;
        String nom = person.getNom();
        String prenom = person.getPrenom();
        if (nom.isEmpty() || prenom.isEmpty()) return null;
        
        Statement statement;
        ResultSet resultat = null;
        
        PersonRW addr;
        if (this.table.equals("appeal")) addr = new PersonRW("clients");
        else addr = new PersonRW("drivers");

        
        try {

            statement = db.connect();
            if (table.equals("offer"))
                resultat = (ResultSet) statement.executeQuery("SELECT * FROM " + "offer" + 
                                                           " WHERE nomchauffeur='" + person.getNom() + 
                                                           "' AND prenomchauffeur='" + person.getPrenom() + 
                                                           "' AND depart='" + drive.getDepart() + "'" );
            else
                resultat = (ResultSet) statement.executeQuery("SELECT * FROM " + "appeal" + 
                                                           "WHERE nomclient='" + person.getNom() + 
                                                           "' AND prenomclient='" + person.getPrenom() + 
                                                           "' AND depart='" + drive.getDepart() + "'" );
            resultat.next();
            drive.setMotif(resultat.getString("motif"));
            drive.setAdresseDepart(resultat.getString("adresseDepart"));
            drive.setAdresseDepart(resultat.getString("adresseArrivee"));
            drive.setCodeDepart(resultat.getString("codeDepart"));
            drive.setCodeArrivee(resultat.getString("codeArrivee"));
            drive.setVilleDepart(resultat.getString("communeDepart"));
            drive.setVilleArrivee(resultat.getString("communeArrivee"));
            drive.setDepart(resultat.getTimestamp("depart"));
            drive.setRetour(resultat.getTimestamp("retour"));
            person.setNom(resultat.getString("nomclient"));
            person.setPrenom(resultat.getString("prenomclient"));
            drive.setDriver(addr.read(person));
            person.setNom(resultat.getString("nomchauffeur"));
            person.setPrenom(resultat.getString("prenomchauffeur"));
            drive.setFlexHeure(resultat.getInt("flexheure"));
            drive.setFlexDate(resultat.getInt("flexdate"));
            drive.setPlaces(resultat.getInt("places"));
            drive.setDriver(addr.read(person));
            
        } catch ( SQLException e ) {

    /* Gérer les éventuelles erreurs ici */

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

    public String writeCSV(HttpServletResponse response, String path)
    {
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition","attachment;filename=" + path);
        ServletOutputStream out;
        try 
        {
            BufferedWriter bw;
            out = response.getOutputStream();
            try (OutputStreamWriter writer = new OutputStreamWriter(out, "UTF-8")) {
                bw = new BufferedWriter(writer);
                List<Drive> drives;
                Person driver, client;
                String line = ""; // writing header
                line += "nomchauffeur;prenomchauffeur;nomclient;prenomclient;adressedepart;adressearrivee;"
                        + "codedepart;codearrivee;"
                        + "communedepart;communearrivee;depart;retour;motif"; 
                bw.write(line);
                bw.newLine();
                drives = readAll();
                // and the data
                for (Drive drive : drives) {
                    driver = drive.getDriver();
                    client = drive.getClient();

                    line =  driver.getNom() + ";" +
                            driver.getPrenom() + ";" +
                            client.getNom() + ";" +
                            client.getPrenom() + ";" +
                            drive.getAdresseDepart() + ";" +
                            drive.getAdresseArrivee() + ";" +
                            drive.getCodeDepart() + ";" +
                            drive.getCodeArrivee() + ";" +
                            drive.getVilleDepart() + ";" +
                            drive.getVilleArrivee() + ";" +
                            drive.getDepart() + ";" +
                            drive.getRetour() + ";" +
                            drive.getMotif() + ";";

                    bw.write(line);
                    bw.newLine();
                }
                bw.flush();
                bw.close();
                out.close();
            }
        }    
        catch(IOException e) 
        {
            Logger.getLogger(PersonRW.class.getName()).log(Level.SEVERE, null, e);
            return "Le fichier n'a pas été trouvé : " + e;
        }
        
        String message = "Clickez ici pour les transports ";
        message += this.table.equals("offer")?"proposés":"demandés";
        message += " !";
        return message;
    }
    
}