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
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.transport.beans.Dispo;
import org.transport.beans.Person;
import org.transport.readwrite.PersonRW;

/**
 *  Processes data retrieved from PostAddress servlet
 *  Communicates back to Servlet or to read/write class Address
 * 
 * 
 *  @author Mael
 */
public class AddressFormat
{
    public static final String DAYS = "dimanche,lundi,mardi,mercredi,jeudi,vendredi,samedi";
   
    private final HttpServletRequest request;
    private final HttpSession session;
    private final String action;
    private final Map<String, String> errors;
    private List<Person> personList;
    private List<String> years;
    private Person person;
    private String personInfo;
    private PersonRW addr;
    private String message;
    
    /**
     *  Parameter 'action' indicates which button has submitted the post
     *  
     *  @param request
     */
    public AddressFormat(HttpServletRequest request)
    {
        this.request = request;
        this.session = request.getSession();
        this.action = request.getParameter("action");
        this.errors = new HashMap<>();
        this.years = new ArrayList();
        this.message = "";
    }

    /**
     *  
     *  called by Address "get" method
     *  'act' indicates 'client or 'driver' address
     *  sets session parameters to start the address actions
     *  
     */
    
    public void info()
    {
        String act = request.getParameter("act");
        if (act ==null) act = "drivers";
        this.personInfo = act.equals("Demandeur")?"clients":"drivers";
        clearAll();
        session.setAttribute("personInfo", this.personInfo); // is it a client or a driver ?
        addr = new PersonRW(this.personInfo);
        this.personList = addr.readAll();
        session.setAttribute("personList", this.personList);
        session.setAttribute("person", person);
        for (int year = 1910; year < 2020; year ++) years.add(Integer.toString(year));
        session.setAttribute("years", years);
        if (this.personInfo.equals("drivers")) session.setAttribute("days", DAYS);
        else session.setAttribute("days","");
        
        request.setAttribute("message", message);
    }

    private void clearAll()
    {
        person = new Person();
        if (personInfo.equals("drivers")) person.setDispo(new Dispo());
        session.setAttribute("lastAction", "");
        request.setAttribute("message", "");
        session.setAttribute("googlemap", null);
    }
  
    /**
     *  
     *  called by Address "post" method
     *  performs 'action' demanded by user
     *  No action ('action' == null) means that an address was selected in the select list
     *  showaddress is called to show the user where the address is geolocated to avoid errors
     *  'Clean'empties inputs of address.jsp
     *  'Confirmer' executes the action demanded the last time (lastAction)
     *  'Supprimer', 'Changer' et 'Ajouter' deletes, changes or adds an address after confirmation
     *  
     */
    
    public void action()
    {
        personInfo = session.getAttribute("personInfo").toString();
        addr = new PersonRW(personInfo);

        if (this.action == null) // Just a selection in the persons list (onchange)
        {
            int offset ;
            if (request.getParameter("search") == null) offset = 0;
            else  offset = Integer.parseInt(request.getParameter("search"));
            List<Person> persons = (List<Person>) session.getAttribute("personList");
            person = persons.get(offset);
            session.setAttribute("googlemap", addr.showAddress(person));
        }
        else 
        {
            getPersonData();                
            if ( errors.isEmpty()) 
                {
                if (this.action.equals("Annuler")) session.setAttribute("lastAction", ""); // clearbutton : lets clear all inputs
                else // another button
                {
                    if (this.action.equals("Confirmer") && session.getAttribute("lastAction") != null)                 
                    {
                        String test = session.getAttribute("lastAction").toString();
                        switch (test)
                        {
                            case "Supprimer":
                                message = addr.delete(person);
                                break;
                            case "Nouveau":
                                message = addr.write(person);
                                break;
                            case "Changer":
                                message = addr.delete(person);
                                message += addr.write(person);
                                break;
                        }
                        if ( message.isEmpty()) 
                        {
                            message += person.getPrenom() + " " + person.getNom();
                            message += (test.equals("Supprimer"))?" a été enlevé(e)":" a été enregistré(e) ";
                        }
                        else errors.put("sql", message);
                        session.setAttribute( "lastAction", "");
                        person = new Person();
                        if (personInfo.equals("drivers")) person.setDispo(new Dispo());
                    }
                    else 
                    {
                        session.setAttribute("lastAction", this.action);
                        session.setAttribute("googlemap", addr.showAddress(person));
                        message = "Est-ce bien cette adresse ? (Confirmez ou recommencez)";
                    }    

                }
            }
         }
        request.setAttribute("message", message);
        session.setAttribute("person", person);
        session.setAttribute("personList", addr.readAll());
        request.setAttribute("erreurs", errors);
    }

    /**
     *  getInfo() Validates all Person info
     *  In case of error the error map is filled in
     *  @return completed Person bean
     */
    private void getPersonData()
    {
        person = new Person();
        String str = this.request.getParameter("nom");
        validationNom(str);
        str = request.getParameter("prenom");
        validationPrenom(str);
        if (this.action.equals("Supprimer")) 
        {
            findPerson();
            return;
        }
        str = request.getParameter("civilite");
        validationCivilite(str);
        str = request.getParameter("adresse");
        validationAdresse(str);
        str = request.getParameter("code");
        validationCode(str);
        str = request.getParameter("ville");
        validationVille(str);
        str = request.getParameter("tel");
        if (str.equals(""))errors.put("tel", "Téléphone pas valide");
        person.setTel(validationTel(str));
        str = request.getParameter("email");
        validationEmail(str);
        str = request.getParameter("naissance");
        validationDate(str, "naissance");
        str = request.getParameter("adhesion");
        validationDate(str, "adhesion");
        boolean signed = "on".equals(request.getParameter("signed"));
        person.setCharte(signed);
        str = request.getParameter("remarques");
        validationRemarques(str);
        if (personInfo.equals("drivers"))
        {
            person.setDispo(new Dispo());
            String days = DAYS;
            for (String day : days.split(","))
            {
                str = request.getParameter(day+"1");
                str += "," + request.getParameter(day+"2");
                validationDay(day, str);
            }
        }
        else
        {
            str = request.getParameter("contact");
            validationContact(str);
            str = request.getParameter("contacttel");
            person.setContacttel(validationTel(str));
        }
    }
    
    /**
     *  Validation for each Person variable
     *  
     *  @param str person variable
     *  @param person Person object
     */
    
    private void validationNom(String str)
    {
        String nom = alphaNumerique(str);
        if (nom == null) errors.put("nom", "Nom pas valide");
        else person.setNom(nom.toUpperCase(Locale.FRANCE));
    }
    
    private void validationPrenom(String str)
    {
        String prenom = alphaNumerique(str);
        if (prenom == null) errors.put("prenom", "Prenom pas valide");
        else person.setPrenom(prenom.substring(0,1).toUpperCase() + prenom.substring(1).toLowerCase());
    }

    private void validationCivilite(String str)
    {
        String civilite = str.equals("Mme")? str : "M.";
        person.setCivilite(civilite);
    }        
        
    private void validationAdresse(String str)
    {
        String adresse = alphaNumerique(str);
        if (adresse == null) errors.put("adresse", "Adresse pas valide");
        else person.setAdresse(adresse);
    }

    private void validationCode(String str)
    {
        String code = str.trim();
        if (code.matches("[0-9]+") && code.length() == 5 && code.substring(0, 2).equals("07"))
        {
            person.setCode(code);
        } else  code = null;
        if (code == null) errors.put("code", "Code postale pas valide (07...)");
    }

    private void validationVille(String str)
    {
        String ville = alphaNumerique(str);
        if (ville == null) errors.put("ville", "Commune pas valide");
        else person.setVille(ville);
    }

    private String validationTel(String str)
    {
        if (str == null) return "";
        String tel = str.trim();
        if (tel.matches("[0-9]+") && tel.length() == 10 && tel.startsWith("0"))
            return tel;
        return "";
    }
    
    private void validationContact(String str)
    {
        String contact = alphaNumerique(str);
        if (contact == null) person.setContact("");
        else person.setContact(contact);
    }
    
    private void validationEmail(String str)
    {
        String email = alphaNumerique(str);
        if (email != null) 
        {
            if (email.contains("@"))
            {
                person.setEmail(email); 
            }
        }   
        else person.setEmail("");
    }

    private void validationDate(String str, String date)
    {
        String sDate;
        Date fdate;
        Date now = Calendar.getInstance().getTime();
        if ( str == null || str.isEmpty()) sDate = "1900" ;
        else sDate = str;

        try 
        {
            if (date.equals("naissance"))
            {
                DateFormat format = new SimpleDateFormat("yyyy");
                fdate=format.parse(sDate);
                person.setNaissance(fdate);
                Calendar cal = Calendar.getInstance();
                cal.setTime(fdate);
                int a = cal.get(Calendar.YEAR);
                cal.setTime(now);
                int b = cal.get(Calendar.YEAR);
    
                if ((personInfo.equals("drivers") && (b-a)<18) || (b-a)>110) 
                {
                    errors.put("date", "La date n'est pas valide");

                }
            }
            else
            {
                    DateFormat format = new SimpleDateFormat("yyyy");
                    fdate=format.parse(sDate);
                    person.setAdhesion(fdate);
            }
        } catch (ParseException ex) 
        {
            Logger.getLogger(AddressFormat.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void validationDay(String day, String str)
    {
        if (str == null) return;
        int begin = parseInt(str.split(",")[0]); // from this hour
        int end = parseInt(str.split(",")[1]); // to this hour
        if (begin > end) errors.put("day", "Début après la fin");
        Dispo dispo = person.getDispo();
        
        switch (day)
        {
            case "dimanche":    dispo.setDimanche(str);
                                break;
            case "lundi":       dispo.setLundi(str);
                                break;
            case "mardi":       dispo.setMardi(str);
                                break;
            case "mercredi":    dispo.setMercredi(str);
                                break;
            case "jeudi":       dispo.setJeudi(str);
                                break;
            case "vendredi":    dispo.setVendredi(str);
                                break;
            case "samedi":      dispo.setSamedi(str);
                                break;
        }
    }
    
    private void validationRemarques(String str)
    {
        String remarques = alphaNumerique(str);
        if (remarques == null) remarques = "";
        person.setRemarques(remarques);
    }

    private String alphaNumerique(String str)
    {
        
        String string = str.trim();
        if (string.isEmpty()) return null;
        for (int i=0; i<str.length(); i++)
        {
            if (string.charAt(i) > 255)  return null;
        }    
        return string;
    }
    
    private void findPerson()
    {
        person = addr.read(person);
        if (person == null) 
        {
            message = "La personne n'est pas dans la base de données";
            errors.put("person", message);
            clearAll();
        }
    }

}
