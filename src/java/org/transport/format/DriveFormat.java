/* 
 * Copyright (C) 2017 Mael
 */
package org.transport.format;

import static java.lang.Integer.parseInt;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.transport.beans.Dispo;
import org.transport.beans.Person;
import org.transport.readwrite.PersonRW;
import org.transport.beans.Distance;
import org.transport.beans.Drive;
import org.transport.readwrite.DriveRW;
import org.transport.readwrite.MapRead;

/**
 *  Called from Drive Servlet to execute actions demanded by posted form of drive.jsp
 *  Calls google MapRead class to find closest persons to selected client
 * 
 * @author Mael
 */
public class DriveFormat
{
    public static final String DAYS = "dimanche,lundi,mardi,mercredi,jeudi,vendredi,samedi";
   
    private final Map<String, String> errors;
    private Person person;
    private Drive drive;
    private final HttpServletRequest request;
    private final HttpSession session;
    private List<Person> personList;
    private List<Drive> driveList;
    private PersonRW addr;
    private DriveRW drv;
    private String action;
    private String personInfo;
    private String driveInfo;
    
    public DriveFormat(HttpServletRequest request)
    {
        this.request = request;
        this.session = request.getSession();
        this.action = request.getParameter("action");
        if (this.action == null) this.action = "";
        errors = new HashMap<>();
    }

    /**
     *  
     *  called by drive "get" method
     *  sets session parameters for drive.jsp page
     *  
     */

    public void info()
    {
        
        if (this.action.equals("Retour")) session.setAttribute("found", false);
        else
        {
            String act = request.getParameter("act");
            session.setAttribute("found", false);
            if (act != null)
            {
                if (act.equals("Demander")) 
                {
                    this.driveInfo = "appeal";
                    this.personInfo = "clients";
                }
                else 
                {
                    this.driveInfo = "offer";
                    this.personInfo = "drivers";
                }
            }
            else 
            {
                this.driveInfo = session.getAttribute("driveInfo").toString();
                this.personInfo = session.getAttribute("personInfo").toString();
            }
            session.setAttribute("personInfo", this.personInfo); 
            session.setAttribute("driveInfo", this.driveInfo); 

            addr = new PersonRW(this.personInfo);
            this.personList = addr.readAll();
            session.setAttribute("personList", this.personList);

            drv = new DriveRW(this.driveInfo);
            this.driveList = drv.readAll();
            session.setAttribute("driveList", this.driveList);

            session.setAttribute("googlemap", null);
            session.setAttribute("message", "");

            person = new Person();

            if (personInfo.equals("drivers")) person.setDispo(new Dispo());
            session.setAttribute("person", person);

            this.drive = new Drive();
            session.setAttribute("drive", this.drive);
        }
    }

    /**
     *  
     *  called by DriveServlet "post" method
     *  If no button has been pushed 
     *  it must be a select client change
     *  that submitted
     *  
     */

    public void action()
    {
        this.personInfo = (String) session.getAttribute("personInfo");
        
        switch (action) {
            case "Trouver":
                distances();    
                break;
            case "Vider":
                info();
                break;
            default:
                selectPerson();
                selectDrive();
                break;
        }
        session.setAttribute("message", "");
    }
   
    /**
     *  
     *  client select changed, so we get info of selected person
     *  
     */

    private void selectPerson()
    {
        if (request.getParameter("searchp") == null) return;        
        int offset =  Integer.parseInt(request.getParameter("searchp"));
        
        this.personList = (List<Person>) session.getAttribute("personList");
        this.personInfo = (String) session.getAttribute("personInfo");
        
        this.drive = new Drive();
            
        this.person = personList.get(offset);
        session.setAttribute("person", this.person);
        
        this.drive.setAdresseDepart(this.person.getAdresse());
        this.drive.setCodeDepart(this.person.getCode());
        this.drive.setVilleDepart(this.person.getVille());
        session.setAttribute("drive", this.drive);

        addr = new PersonRW(personInfo);
        session.setAttribute("googlemap", this.addr.showAddress(this.person, new ArrayList<Person>()));
    }
    
    
    /**
     *  
     *  drive select changed, so we get info of selected person
     *  
     */

    private void selectDrive()
    {
        if (request.getParameter("searchd") == null) return;
        int offset =  Integer.parseInt(request.getParameter("searchd"));
        this.driveList = (List<Drive>) session.getAttribute("driveList");
        this.driveInfo = (String) session.getAttribute("driveInfo");
        
        this.drive = new Drive();
            
        this.drive = driveList.get(offset);
        session.setAttribute("drive", this.drive);
        person = driveInfo.equals("appeal")?this.drive.getClient():this.drive.getDriver();
        session.setAttribute("person", person);

        addr = new PersonRW(personInfo);
        session.setAttribute("googlemap", this.addr.showAddress(this.person, new ArrayList<Person>()));
    }
    
    /**
     *  
     *  with google read and distances we find the closest drivers for our client
     *  
     */

    private void distances()
    {
        this.person = (Person) session.getAttribute("person");
        getDriveData();
        if (errors.isEmpty())
        {
            List<Person> persList = new ArrayList<>();
            addr = new PersonRW(this.personInfo.equals("drivers")?"clients":"drivers");
            List<Person> persons = addr.readAll();

            MapRead google = new MapRead(); 
            this.session.setAttribute("message",google.read(this.drive, persons));

            int i=0;
            List<Distance> d = google.getDistances();
            List<Distance> distances = new ArrayList<>();
            for (Distance distance : d)
            {
                if (dateOK(distance))
                {
                    distances.add(distance);
                    persList.add(distance.getPerson());
                }
                if (i++ > 20) break;
            }
            session.setAttribute("googlemap", addr.showAddress(drive, persList));
            session.setAttribute("distanceList", distances);
            session.setAttribute("found", true);
        }
        else 
        {
            session.setAttribute("found", false);
            request.setAttribute("erreurs", errors);
        }
    }
    
    /**
     *  
     *  We must validate the given data, 
     *  One never knows who's behind the keyboard
     *  
     */

    private void getDriveData()
    {
        this.drive = new Drive();
        if (this.personInfo.equals("clients"))
            this.drive.setClient(person);
        else this.drive.setDriver(person);
        validationAdresses();
        validationCodes();
        validationVilles();
        validationDate();
        validationMotif();
        try {
        drive.setPlaces(Integer.parseInt(request.getParameter("places")));
        } catch(NumberFormatException e) {drive.setPlaces(1);}
        try {
        drive.setFlexHeure(Integer.parseInt(request.getParameter("flexheure")));
        } catch(NumberFormatException e) {drive.setFlexHeure(0);}
        try {
        drive.setFlexDate(Integer.parseInt(request.getParameter("flexdate")));
        } catch(NumberFormatException e) {drive.setFlexDate(0);}

        session.setAttribute("drive", this.drive);
    }
    
    /**
     *  Validation for each Drive variable
     *  
     */
    
    private void validationAdresses()
    {
        String adresse;
        
        String str = request.getParameter("adresseDepart");
        if (str == null) adresse = this.person.getAdresse();
        else adresse = alphaNumerique(str);
        if (adresse == null) 
        {
            errors.put("adresseDepart", "Adresse non valide");
            this.drive.setAdresseDepart(this.person.getAdresse());
        }
        else this.drive.setAdresseDepart(adresse);
        
        str = request.getParameter("adresseArrivee");
        adresse = alphaNumerique(str);
        if (adresse == null)
        {
            errors.put("adresseArrivee", "Adresse non valide");
            this.drive.setAdresseArrivee("");
        }
        else this.drive.setAdresseArrivee(adresse);
    }

    private void validationCodes()
    {
        String str = request.getParameter("codeDepart");
        String code = str.trim();
        code = (code.matches("[0-9]+") && code.length() == 5)?code:"";
        if (code.equals("")) errors.put("codeDepart", "Code non valide");
        this.drive.setCodeDepart(code);
        str = request.getParameter("codeArrivee");
        code = str.trim();
        code = (code.matches("[0-9]+") && code.length() == 5)?code:"";
        if (code.equals("")) errors.put("codeArrivee", "Code non valide");
        this.drive.setCodeArrivee(code);
    }

    private void validationVilles()
    {
        String ville;
        
        String str = request.getParameter("villeDepart");
        if (str == null) ville = this.person.getVille();
        else ville = alphaNumerique(str);
        if (ville == null) 
        {
            errors.put("villeDepart", "Ville non valide");
            this.drive.setVilleDepart(this.person.getVille());
        }
        else this.drive.setVilleDepart(ville);
        
        str = request.getParameter("villeArrivee");
        ville = alphaNumerique(str);
        if (ville == null)
        {
            errors.put("villeArrivee", "Ville non valide");
            this.drive.setVilleArrivee("");
        }
        else this.drive.setVilleArrivee(ville);
    }

    private void validationDate()
    {
        Date now = Calendar.getInstance().getTime();
        String depart = request.getParameter("depart");
        String retour = request.getParameter("retour");
        
        try {
            DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            if (depart != null) drive.setDepart((format.parse(depart)));
            else drive.setDepart(now);
            if (retour != null && !retour.isEmpty()) drive.setRetour(format.parse(retour));
        } catch (ParseException ex) {
            errors.put("dateDepart", "Date départ non valide");
        }
        if (drive.getDepart() != null);
            /* For the moment we prefer be able to input afterwards
            if (drive.getDepart().before(now)) 
            {
                errors.put("dateDepart", "La date départ est déjà passé");
                drive.setDepart(now);
            }
            */
        if (drive.getRetour() != null && !drive.getRetour().equals(""))
            if (drive.getRetour().before(drive.getDepart())) 
            {
                errors.put("dateRetour", "Le retour est avant le départ");
                drive.setRetour(null);
            }                
    }

    private void validationMotif()
    {
        String str = request.getParameter("motif");
        String motif = alphaNumerique(str);
        if (motif == null) motif = "";
        drive.setMotif(motif);
    }
    
    private boolean dateOK(Distance dist)
    {
        if (personInfo.equals("drivers") ) return true;
        Person driver = dist.getPerson();
        
        int distance = dist.getMeter();
        int time = distance/1000;  // approximate drive time in minutes (60 km/hour)
        
        Calendar depart = Calendar.getInstance();
        depart.setTime(drive.getDepart());
        int jourDepart = depart.get(Calendar.DAY_OF_WEEK);
        

        Calendar retour = Calendar.getInstance();
        retour.setTime(drive.getDepart());
        int jourRetour = retour.get(Calendar.DAY_OF_WEEK);
        
        String jour = DAYS.split(",")[jourDepart-1]; // which day is it (sunday, monday, tuesday...)
        String hours;
        Dispo dispo = driver.getDispo();
        dispo.setJour(jour);
        hours = dispo.getJour(); // is our driver available this day?
        
        int heureDepart = depart.get(Calendar.HOUR_OF_DAY) * 60 + depart.get(Calendar.MINUTE);
        int heureRetour = retour.get(Calendar.HOUR_OF_DAY) * 60 + retour.get(Calendar.MINUTE);
        int heure1 = parseInt(hours.split("-")[0]) * 60;
        int heure2 = parseInt(hours.split("-")[1]) * 60 - time;
        return heureDepart >= heure1 && heureRetour < heure2; // at this time ?
    }
    
    private String alphaNumerique(String str)
    {
        if (str == null || str.isEmpty()) return null;
        String string = str.trim();
        for (int i=0; i<str.length(); i++)
        {
            if (string.charAt(i) > 255) return null;
        }
        return string;
    }
    
}
