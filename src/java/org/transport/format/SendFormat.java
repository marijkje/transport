/* 
 * Copyright (C) 2017 Mael
 */
package org.transport.format;

import static java.lang.Integer.parseInt;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.transport.beans.Person;
import org.transport.beans.Distance;
import org.transport.beans.Drive;
import org.transport.readwrite.DriveRW;
import org.transport.readwrite.Email;
import org.transport.readwrite.PersonRW;

/**
 *  Called from Send Servlet to execute actions demanded 
 *  by get form of drive.jsp to gather information for email to clients
 *  Or post form of send.jsp 
 *  to actually send emails
 * 
 * @author Mael
 */
public class SendFormat
{
    private final Person person;
    private final Drive drive;
    private final List<Distance> distances;
    private final HttpServletRequest request;
    private final HttpSession session;
    private final String personInfo;
    private String action;
    private String text;
    private String subject;
    private String message;
    private final Map<String, String> errors;
    private List<String> toMails;
        
    
    public SendFormat(HttpServletRequest request)
    {
        this.errors = new HashMap<>();
        this.request = request;
        this.session = request.getSession();

        this.distances = (List<Distance>) session.getAttribute("distanceList");
        
        this.personInfo = (String) session.getAttribute("personInfo");
        this.person = (Person) session.getAttribute("person");
        this.drive = (Drive) session.getAttribute("drive");
        
        this.toMails = new ArrayList<>();
        this.action = request.getParameter("action");
        if (this.action == null) this.action = "";
        this.message = "";
        request.setAttribute("message", message);
    }

    /**
     *  
     *  called by Send "get" method
     *  sets session parameters to prepare the email data
     *  
     */
    public void info()
    {
        String selected[] = request.getParameterValues("selected"); 
        int i;
        if (selected != null  && selected.length != 0)
            for (i = 0; i < selected.length; i++)
                toMails.add(this.distances.get(parseInt(selected[i])).getPerson().getEmail());
        if (personInfo.equals("drivers")) setDriverText();
        else  setClientText();       
        
        session.setAttribute("text", text);
        session.setAttribute("subject", subject);
        session.setAttribute("recipiants", toMails);
        session.setAttribute("send", true);
    }

    private void setDriverText()
    {
        text = "Bonjour,\n\n";

        text += "Le :  " + drive.getDateDepart() + "\n";
        text += "Chauffeur :  " + person.getPrenom() + " " + person.getNom() + "\n";
        text += "Va de :  " + drive.getAdresseDepart();
        text += " dans " + drive.getVilleDepart() + "\n";
        text += "A :  " + drive.getAdresseArrivee();
        text += " dans " + drive.getVilleArrivee() + "\n";
        if (drive.getRetour() != null && !drive.getRetour().equals("")) text += "Retour prévu pour : " + drive.getDateRetour() + "\n";

        text += "Merci de nous faire savoir si ce voyage vous intéresse\n \n";
        
        subject = "Un transport est proposé !";
    }
    
    private void setClientText()
    {
        text = "Bonjour,\n\n";

        text += "Le :  " + drive.getDateDepart() + "\n";
        text += "Demandeur :  " + person.getPrenom() + " " + person.getNom() + "\n";
        text += "Doit aller de :  " + drive.getAdresseDepart();
        text += " dans " + drive.getVilleDepart() + "\n";
        text += "A :  " + drive.getAdresseArrivee();
        text += " dans " + drive.getVilleArrivee() + "\n";
        if (drive.getRetour() != null && !drive.getRetour().equals("")) text += "Retour prévu pour : " + drive.getDateRetour() + "\n";

        text += "Merci de nous faire savoir si vous pouvez faire ce voyage\n\n";

        subject = "Un transport est demandé !";
        
    }
    
    /**
     *  
     *  called by Send "post" method
     *  'retour' button sends back to 'drive' page
     *  'envoyer' button sends a mail to clients who might be interested
     *  
     *  @return page to go to
     */

    public String action()
    {
        switch (this.action) {
            case "Envoyer":     setDriveData();
                                if (errors.isEmpty())
                                {
                                    sendEmails();
                                    session.setAttribute("send", false);
                                }
                                return "/JSP/user/send.jsp";
            default:            return "/JSP/user/drive.jsp";
        }
    }
    
   
    private void sendSMS()
    {
        // When we have the budget necessary for this action we'll look into it...
    }
    
    private void sendEmails()
    {
        Email email = new Email();
        
        message += email.send(toMails, subject, text);
        session.setAttribute("text", message);
    }
    
    private void setDriveData()
    {
        validationText(request.getParameter("text"));
        if (!text.isEmpty())
        {
            transformText();
            subject = (String) session.getAttribute("subject");
            toMails = (List<String>) session.getAttribute("recipiants");
//            String personInfo = (String) session.getAttribute("personInfo");
            DriveRW drv = new DriveRW(personInfo.equals("drivers")?"offer":"appeal");
            message = drv.write(drive);
        }
    }
    
    /**
     *  Validation for email text
     *  
     */
    
    private void validationText(String str)
    {
        if (!isAscii(str))
        {
            errors.put("nom", "Erreurs dans le texte, recommencez");
            request.setAttribute("erreurs", errors);
            text = "";
        }
        else text = str;
    }
    
    /**
     *  Validation for email text
     *  
     */
    
    private void transformText()
    {
        text = "<div>" + text;
        text = text.replace("\r\n\r\n", "</div><br/><div>");
        text = text.replace("\r\n", "</div><div>");
        if (personInfo.equals("drivers"))
        {
        text += "<form style=\"float: left;\" method='post' action='mailto:transportsolidaire07@gmail.com?"
                + "subject=OUI%2C%20je%20suis%20disponible"
                + "&body=OUI%2C%20je%20suis%20"
                + "int%C3%A9ress%C3%A9%0A%0D%0ANom%20%3A%0D%0APr%C3%A9nom%20%3A%0AT%C3%A9l%C3%A9phone%20%3A%0A'>"
                + "<button style=\"background-color:green; color:white; height:60px; width:200px;\">Oui, ça m'intéresse !</button></form>";
        text += "<a href='mailto:transportsolidaire07@gmail.com?"
                + "subject=NON%2C%20d%C3%A9sol%C3%A9"
                + "&body=NON%2C%20je%20ne%20suis%20pas%20int%C3%A9ress%C3%A9%20cette%20fois'>"
                + "<button style=\"background-color:red; color:white; height:60px; width:200px;\">Non, merci...</button></form></div>";
        }
        else
        {
        text += "<form style=\"float: left;\" method='post' action='mailto:transportsolidaire07@gmail.com?"
                + "subject=OUI%2C%20je%20suis%20disponible"
                + "&body=OUI%2C%20je%20suis%20"
                + "disponible%0D%0A%0D%0ANom%20%3A%0D%0APr%C3%A9nom%20%3A%0AT%C3%A9l%C3%A9phone%20%3A%0A'>"
                + "<button style=\"background-color:green; color:white; height:60px; width:200px;\">Oui, je suis disponible</button></form>";
        text += "<form method='post' action='mailto:transportsolidaire07@gmail.com?"
                + "subject=NON%2C%20d%C3%A9sol%C3%A9"
                + "&body=NON%2C%20d%C3%A9sol%C3%A9%20je%20ne%20suis%20pas%20disponible%20cette%20fois'>"
                + "<button style=\"background-color:red; color:white; height:60px; width:200px;\">Non, désolé</button></form></div>";
        }
    }
    
    private boolean isAscii(String str)
    {
        if (str == null) return false;
        for (int i=0; i<str.length(); i++)
        {
            if (str.charAt(i) > 255) return false;
        }
        return true;
    }

}
