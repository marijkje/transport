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
import org.transport.beans.Dispo;
import org.transport.beans.Drive;
import org.transport.beans.Person;
import org.transport.format.AddressFormat;

/**
 *  Performs read/write actions with client and driver databases
 *  Called by Addressform
 *  
 * @author Mael
 */
public class PersonRW
{
    public static final String DAYS = "dimanche,lundi,mardi,mercredi,jeudi,vendredi,samedi";
   
    private final String table;
    private final BaseRW db;
    private String error;
    
    public PersonRW(String personInfo)
    {
        this.table = personInfo;
        this.db = new BaseRW();
    }

    public String delete(Person person)
    {
        error = db.error();
    
        if (error.isEmpty())
        {
            DriveRW drives = new DriveRW(this.table.equals("clients")?"appeal":"offer");

            drives.deleteAll(person); // If we remove a person, we have to remove his drives

            Statement statement;

            try {
                statement = db.connect();
                statement.executeUpdate("DELETE FROM " + this.table + 
                        " WHERE nom='" + person.getNom() + "' AND prenom='" + person.getPrenom() + "'");

            } catch ( SQLException e ) {
                switch (e.getSQLState())
                        {
                    case "23505":     error += "Cette personne n'est pas dans la liste";
                                      break;
                    default:          error += e.getErrorCode() + " " + e.getSQLState();        
                }

            } 
        }    
        db.close();
        return error;
    }

    
    public String write(Person person)
    {
        Dispo dispo;
        String days = DAYS;
        String message = "";
        String sel = " (nom, prenom, civilite, adresse, code, commune, tel, email, naissance, places, adhesion , charte, ";
        if (this.table.equals("drivers")) sel += days +", ";
        else sel += "contact, contacttel,";
        sel += " remarques)";
        String val = "( '"  + person.getNom() + "', '"
                            + person.getPrenom() + "', '"
                            + person.getCivilite() + "', '"
                            + person.getAdresse() + "', '"
                            + person.getCode() + "', '"
                            + person.getVille() + "', '"
                            + person.getTel() + "', '"
                            + person.getEmail() + "', '"
                            + person.getNaissance() + "', '"
                            + person.getPlaces() + "', '"
                            + person.getAdhesion() + "', '"
                            + person.getCharte() + "', '";
        if (this.table.equals("drivers"))
        {
            dispo = person.getDispo();
            for(String day : days.split(","))
            {
                dispo.setJour(day);
                val += dispo.getJour() + "', '";
            }
        }
        else 
        {    
            val += person.getContact() + "', '";
            val += person.getContacttel() + "', '";
        }
        val += person.getRemarques() + "' )";

        Statement statement;
        if (db.error().isEmpty())
        {

            try {
                statement = db.connect();
                statement.executeUpdate("INSERT INTO " + this.table + sel + " VALUES " + val + "");
            } catch ( SQLException e ) {
                switch (e.getSQLState())
                        {
                    case "23505":     message += "Cette personne est déja dans la liste";
                                      break;
                    default:          message += e.getMessage() + " " + e.getSQLState() + " " + val + " " + sel;        
                }

            } 
        }    
        error = db.error();
        db.close();
        return message;
    }
    
    public List<Person> readAll()
    {
        String str = "SELECT * FROM " + this.table + " ORDER BY nom, prenom";
        List<Person> all = new ArrayList<>();

        Statement statement;
        ResultSet resultat = null;
        if (db.error().isEmpty())
        {
        
            try {
                statement = db.connect();
                if (statement!=null)
                {
                    resultat = (ResultSet) statement.executeQuery(str);
                    while (resultat.next())
                    {
                        Person person = new Person();
                        person.setNom(resultat.getString("nom"));
                        person.setPrenom(resultat.getString("prenom"));
                        person.setCivilite(resultat.getString("civilite"));
                        person.setAdresse(resultat.getString("adresse"));
                        person.setCode(resultat.getString("code"));
                        person.setVille(resultat.getString("commune"));
                        person.setTel(resultat.getString("tel"));
                        person.setEmail(resultat.getString("email"));
                        person.setNaissance(resultat.getDate("naissance"));
                        person.setAdhesion(resultat.getDate("adhesion"));
                        person.setRemarques(resultat.getString("remarques"));
                        person.setNombre(resultat.getInt("nombre"));
                        person.setCharte(resultat.getBoolean("charte"));
                        if (this.table.equals("drivers"))
                        {
                            person.setPlaces(resultat.getInt("places"));
                            Dispo dispo = new Dispo();
                            dispo.setLundi(resultat.getString("lundi"));
                            dispo.setMardi(resultat.getString("mardi"));
                            dispo.setMercredi(resultat.getString("mercredi"));
                            dispo.setJeudi(resultat.getString("jeudi"));
                            dispo.setVendredi(resultat.getString("vendredi"));
                            dispo.setSamedi(resultat.getString("samedi"));
                            dispo.setDimanche(resultat.getString("dimanche"));
                            person.setDispo(dispo);
                        }
                        else 
                        {    
                            person.setContact(resultat.getString("contact"));
                            person.setContacttel(resultat.getString("contacttel"));
                        }
                        all.add(person);
                    }
                }
            } catch ( SQLException e ) {
                error = "Could not read table";
                return null;

            } finally {
            if ( resultat != null )
                try {
                    resultat.close();

                    } catch ( SQLException ignore ) {
                }
            
            }
        }
        error = db.error();
        db.close();
        return all;
    }

    /**
     *  With name and last name the persons information is retrieved from database
     *  
     * @param person
     * @return 
    */
    public Person read(Person person)
    {
        if (person == null || person.getNom() == null) return null;
        String nom = person.getNom();
        String prenom = person.getPrenom();
        if (nom.isEmpty() || prenom.isEmpty()) return null;
        Dispo dispo = new Dispo();

        Statement statement;
        ResultSet resultat = null;
        if (db.error().isEmpty())
        {
            try {
                statement = db.connect();
                resultat = (ResultSet) statement.executeQuery("SELECT * FROM " + this.table + 
                                                               " WHERE nom='" + nom+ 
                                                               "' AND prenom='" + prenom+ "'" );
                resultat.next();
                if (!resultat.isAfterLast())
                {
                    person.setCivilite(resultat.getString("civilite"));
                    person.setAdresse(resultat.getString("adresse"));
                    person.setCode(resultat.getString("code"));
                    person.setVille(resultat.getString("commune"));
                    person.setTel(resultat.getString("tel"));
                    person.setEmail(resultat.getString("email"));
                    person.setNaissance(resultat.getDate("naissance"));
                    person.setAdhesion(resultat.getDate("adhesion"));
                    person.setRemarques(resultat.getString("remarques"));
                    person.setNombre(resultat.getInt("nombre"));
                    person.setCharte(resultat.getBoolean("charte"));
                    if (this.table.equals("drivers"))
                    {
                        person.setPlaces(resultat.getInt("places"));
                        dispo.setLundi(resultat.getString("lundi"));
                        dispo.setMardi(resultat.getString("mardi"));
                        dispo.setMercredi(resultat.getString("mercredi"));
                        dispo.setJeudi(resultat.getString("jeudi"));
                        dispo.setVendredi(resultat.getString("vendredi"));
                        dispo.setSamedi(resultat.getString("samedi"));
                        dispo.setDimanche(resultat.getString("dimanche"));
                        person.setDispo(dispo);
                    }
                    else 
                    {    
                        person.setContact(resultat.getString("contact"));
                        person.setContacttel(resultat.getString("contacttel"));
                    }
                    person.setRemarques(resultat.getString("remarques"));
                }
            } catch ( SQLException e ) {

                Logger.getLogger(AddressFormat.class.getName()).log(Level.SEVERE, null, e);
                return null; 

            } finally {
            if ( resultat != null )
                try {
                    resultat.close();

                    } catch ( SQLException ignore ) {
                }
            }
        }    
        error = db.error();
        db.close();
        return person;
    }

    
    
    public String increment(Person person)
    {
        String message = "";
        Statement statement;
        ResultSet resultat = null;

        if (db.error().isEmpty())
        {
            try {
                statement = db.connect();
                resultat = (ResultSet) statement.executeQuery("UPDATE " + this.table + 
                                                              " SET nombre = nombre + 1 " +
                                                              "WHERE nom='" + person.getNom() +
                                                              "' AND prenom='" + person.getPrenom() + "'" +
                                                              "RETURNING nombre");       
                resultat.next();
            } catch ( SQLException e ) {

                return "error incrementing counter : " + e + "\n"; 

            } finally {
            if ( resultat != null )
                try {
                    resultat.close();

                    } catch ( SQLException ignore ) {
                }
            }
        }
        error = db.error();
        db.close();
        return message;
     }

    public String decrement(Person person)
    {
        String message = "";
        Statement statement;
        ResultSet resultat = null;

        if (db.error().isEmpty())
        {
            try {
                statement = db.connect();
                resultat = (ResultSet) statement.executeQuery("UPDATE " + this.table + 
                                                              " SET nombre = nombre - 1 " +
                                                              "WHERE nom='" + person.getNom() +
                                                              "' AND prenom='" + person.getPrenom() + "'" +
                                                              "RETURNING nombre");       
                resultat.next();
            } catch ( SQLException e ) {

                return "error decrementing counter : " + e + "\n"; 

            } finally {
            if ( resultat != null )
                try {
                    resultat.close();

                    } catch ( SQLException ignore ) {
                }
            }
        }    
        error = db.error();
        db.close();
        return message;
     }

    
    public String writeCSV(HttpServletResponse response, String path)
    {
        String message;
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition","attachment;filename=" + path);
        ServletOutputStream out = null;
        try 
        {
            BufferedWriter bw;
            out = response.getOutputStream();
            try (OutputStreamWriter writer = new OutputStreamWriter(out, "UTF-8")) {
                bw = new BufferedWriter(writer);
                List<Person> persons = readAll();
                // writing header
                String line = ""; 
                line += "nom;prenom;civilite;adresse;code;commune;tel;email;naissance;adhesion;charte;";
                if (this.table.equals("drivers"))
                {
                    for(String day : DAYS.split(","))
                        line += day + ";";
                }
                else line += "contact; ";
                line += "remarques";
                bw.write(line);
                bw.newLine();
                // and the data
                for (Person person : persons)
                {
                    line =  person.getNom() + ";" +
                            person.getPrenom()+ ";" +
                            person.getCivilite()+ ";" +
                            person.getAdresse()+ ";" +
                            person.getCode()+ ";" +
                            person.getVille()+ ";" +
                            person.getTel()+ ";" +
                            person.getEmail()+ ";" +
                            person.getNaissance()+ ";" +
                            person.getAdhesion()+ ";" +
                            person.getCharte()+ ";";
                    if (this.table.equals("drivers"))
                    {
                        Dispo dispo = person.getDispo();
                        for(String day : DAYS.split(","))
                        {
                            dispo.setJour(day);
                            line += dispo.getJour() + ";";
                        }
                    }
                    else
                    {
                        line += person.getContact() + ";";
                        line += person.getContacttel() + ";";
                    }
                    line += person.getRemarques();

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
        message = "Clickez ici pour la liste de ";
        message += this.table.equals("drivers")?"chauffeurs":"demandeurs";
        message += " !";
        return message;
    }
    
    
    /**
     *  Shows the environment of the person's address and sends message to ask confirmation of the address
     *  puts the url in the session variable 'googlemap' 
     * 
     * @param person
     * @param persons
     * @return message
     */
    public String showAddress(Person person, List<Person> persons)
    {
        String mapUrl = "";
        String addr = "";
        if (person == null)  return "";
        
        addr += "&center=" + person.getAdresse().replace(' ', '+') + "," + person.getVille().replace(' ', '+');
        addr += "&markers=icon:https://raw.githubusercontent.com/marijkje/transport/master/web/inc/images/marker.png|" 
                + person.getAdresse().replace(' ', '+') + "," + person.getVille().replace(' ', '+');
        if (!persons.isEmpty())addr += "&markers=color:green";
//        addr = persons.stream().map((pers) -> "|" + pers.getAdresse().replace(' ', '+') + "," + pers.getVille().replace(' ', '+')).reduce(addr, String::concat);
        MapRead map = new MapRead();
        try
        {
            mapUrl = map.getMap(addr, "320x450");
        }
        catch (Exception ex)
        {
            Logger.getLogger(AddressFormat.class.getName()).log(Level.SEVERE, null, ex);
        }
        return mapUrl;
    }
    
    
    /**
     *  Shows the environment of the person's address and sends message to ask confirmation of the address
     *  puts the url in the session variable 'googlemap' 
     * 
     * @param drive
     * @param persons
     * @return message
     */
    public String showAddress(Drive drive, List<Person> persons)
    {
        String mapUrl = "";
        String addr = "";
        if (drive == null)  return "";
        
        addr += "&center=" + drive.getAdresseDepart().replace(' ', '+') + "," + drive.getVilleDepart().replace(' ', '+');
        addr += "&markers=icon:https://raw.githubusercontent.com/marijkje/transport/master/web/inc/images/marker.png|" 
                + drive.getAdresseDepart().replace(' ', '+') + "," + drive.getVilleDepart().replace(' ', '+');
        if (!persons.isEmpty())addr += "&markers=color:green";
//        addr = persons..stream().map((pers) -> "|" + pers.getAdresse().replace(' ', '+') + "," + pers.getVille().replace(' ', '+')).reduce(addr, String::concat);
        MapRead map = new MapRead();
        try
        {
            mapUrl = map.getMap(addr, "320x450");
        }
        catch (Exception ex)
        {
            Logger.getLogger(AddressFormat.class.getName()).log(Level.SEVERE, null, ex);
        }
        return mapUrl;
    }
    
    
    public String showAddress(Person person)
    {
        String mapUrl = "";
        String addr = "";
        if (person == null)  return "";
        
        addr += "&center=" + person.getAdresse().replace(' ', '+') + "," + person.getVille().replace(' ', '+');
        addr += "&markers=icon:https://raw.githubusercontent.com/marijkje/transport/master/web/inc/images/marker.png|" 
                + person.getAdresse().replace(' ', '+') + "," + person.getVille().replace(' ', '+');
        
        MapRead map = new MapRead();
        try
        {
            mapUrl = map.getMap(addr, "320x450");
        }
        catch (Exception ex)
        {
            Logger.getLogger(AddressFormat.class.getName()).log(Level.SEVERE, null, ex);
        }
        return mapUrl;
    }
    
    public String error()
    {
    return error;
    }
}    
