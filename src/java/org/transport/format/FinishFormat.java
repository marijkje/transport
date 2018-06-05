/* 
 * Copyright (C) 2017 Mael
 */
package org.transport.format;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.transport.beans.Person;
import org.transport.readwrite.PersonRW;
import org.transport.beans.Drive;
import org.transport.readwrite.DoneRW;
import org.transport.readwrite.DriveRW;
import org.transport.readwrite.MapRead;

/**
 *  Called from FinishSV Servlet to execute actions demanded by posted form of finish.jsp
 *  Calls Google MapRead class to find distance traveled
 * 
 * @author Mael
 */
public class FinishFormat
{
    private String message;
    private List<Drive> driveList;
    private List<Person> personList;
    private final HttpServletRequest request;
    private final HttpSession session;
    private String action;
    private String personInfo;
    private String driveInfo;
    
    public FinishFormat(HttpServletRequest request)
    {
        this.request = request;
        this.session = request.getSession();
        this.action = request.getParameter("action");
        if (this.action == null) this.action = "";
        this.message = "";
    }

    /**
     *  
     *  called by finish "get" method
     *  sets session parameters for drive.jsp page
     *  
     */

    public void info()
    {
        session.setAttribute("what", "nothing");
        session.setAttribute("confirm", false);
        session.setAttribute("message", "");
    }

    /**
     *  called by FinishServlet "post" method
     *  If no button has been pushed 
     *  it must be a select drive or person change
     *  that submitted
     *  
     */

    public void action()
    {
        switch(action) 
        {
            case "Offre":       getOffers();
                                break;
            case "Demande":     getAppeals();
                                break;
            case "Archive":     getDones();
                                break;
            case "Finaliser":   
            case "Effacer":     prepare();
                                break;
            case "Confirmer":   finish();
                                break;
            case "Retour":      session.setAttribute("what", "nothing");                    
                                break;
            default:            this.driveInfo = (String) session.getAttribute("driveInfo");
                                if (this.driveInfo.equals("done")) selectDone();
                                else selectDrive();
        }
    }
   
    /**
     *  
     *  drive or person select changed, so we get info of selected drive
     *  
     */

    private void selectDrive()
    {
        this.driveList = (List<Drive>) session.getAttribute("driveList");
        this.personList = (List<Person>) session.getAttribute("personList");

        Drive drive;
        Person person;
        
        if (driveList != null && !driveList.isEmpty())    
        {
            int offset ;
            if (request.getParameter("drive") == null) offset = 0;
            else  offset = Integer.parseInt(request.getParameter("drive"));
            drive = driveList.get(offset);
        }
        else drive = new Drive();
        session.setAttribute("drive", drive);

        if (personList != null && !personList.isEmpty())
        {
            int offset;
            if (request.getParameter("person") == null) offset = 0;
            else  offset = Integer.parseInt(request.getParameter("person"));
            person = personList.get(offset);
        }
        else person = new Person();
        session.setAttribute("person", person);
        
        session.setAttribute("confirm", false);
    }

    /**
     *  
     *  archive select changed, so we get info of selected drive
     *  
     */

    private void selectDone()
    {
        this.driveList = (List<Drive>) session.getAttribute("driveList");

        Drive drive;
        Person person;
        
        if (driveList != null && !driveList.isEmpty())    
        {
            int offset ;
            if (request.getParameter("drive") == null) offset = 0;
            else  offset = Integer.parseInt(request.getParameter("drive"));
            drive = driveList.get(offset);
        }
        else drive = new Drive();
        session.setAttribute("drive", drive);

        session.setAttribute("confirm", false);
    }

    /**
     *  
     *  set personInfo and driveInfo
     *  in the 'offer' database
     *  
     */

    private void getOffers()
    {
        this.personInfo = "clients";
        this.driveInfo = "offer";
        getDrives();
    }
    
    /**
     *  
     *  set personInfo and driveInfo
     *  in the 'appeal' database
     *  
     */

    private void getAppeals()
    {
        this.personInfo = "drivers";
        this.driveInfo = "appeal";
        getDrives();
    }
    
    /**
     *  
     *  And get the information in the 'done' database
     *  
     */

    private void getDones()
    {
        this.driveInfo = "done";

        DoneRW drv = new DoneRW();
        this.driveList = drv.readAll();

        if (driveList == null) driveList = new ArrayList<>();

        session.setAttribute("driveInfo", this.driveInfo);
        session.setAttribute("driveList", this.driveList);

        session.setAttribute("what", "action");

        selectDone();
    }
    
    /**
     *  
     *  get the information in the databases
     *  
     */

    private void getDrives()
    {
        DriveRW drv = new DriveRW(this.driveInfo);
        this.driveList = drv.readAll();

        if (driveList == null) driveList = new ArrayList<>();

        session.setAttribute("driveList", this.driveList);
        session.setAttribute("driveInfo", this.driveInfo);

        PersonRW addr = new PersonRW(this.personInfo);
        this.personList = addr.readAll();
        
        if (personList == null) personList = new ArrayList<>();
        session.setAttribute("personList", this.personList);
        session.setAttribute("personInfo", this.personInfo);

        session.setAttribute("what", "action");

        selectDrive();
    }
    
    /**
     *  
     *  Get the 'done' database and put it in the drivelist and personlist parameters
     *  
     */

    private void prepare()
    {

        selectDrive();
        this.message = "Etes vous sûre de vouloir " + this.action + " ce voyage ?";
        session.setAttribute("lastAction", this.action);
        session.setAttribute("confirm", true);
        session.setAttribute("message", this.message);
    }
    
    private void finish()
    {
        MapRead map = new MapRead();
        Drive drive = (Drive)session.getAttribute("drive");
        Person person = (Person) session.getAttribute("person");
        this.driveInfo = (String) session.getAttribute("driveInfo");
        try {
            drive.setKilometres(map.getKilometers(drive, person, driveInfo));
        } catch(NumberFormatException e) {drive.setKilometres(0);}
        
        if (session.getAttribute("lastAction").equals("Finaliser"))
        {
            DoneRW done = new DoneRW();
            this.message = done.write(drive, person, driveInfo);
        }
        else 
        {
            if (driveInfo.equals("done"))
            {    
                DoneRW done = new DoneRW();
                this.message = done.delete(drive);
            }
            else
            {
                DriveRW drv = new DriveRW(driveInfo);
                this.message = drv.delete(drive);
            }
        }
        session.setAttribute("confirm", false);
        session.setAttribute("message", this.message);
        info();
    }
    
}