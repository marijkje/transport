/* 
 * Copyright (C) 2017 Mael
 */
package org.transport.format;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.transport.readwrite.PersonRW;
import org.transport.readwrite.DoneRW;
import org.transport.readwrite.DriveRW;

/**
 * Get database and transform in csv
 * Upload to user
 * @author Mael
 */
public class DownloadFormat {
   
//    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private final HttpSession session;
    private String personInfo;
    private String driveInfo;
    private final String action;
    private String message;
    private String filePath;
    
    public DownloadFormat(HttpServletRequest request, HttpServletResponse response)
    {
        this.response = response;
        this.session = request.getSession();
        this.action = request.getParameter("action");
        this.message = "";
    }

    public void info()
    {
        message = "Quel fichier télécharger ?"; 
        session.setAttribute("message", message);
        session.setAttribute("url", "");
    }
    
    public void action()
    {
        this.filePath = "downloads/";
        
        switch(action)
        {
            case "Chauffeur":   personInfo = "drivers";
                                getPersonList();
                                break;
            case "Demandeur":   personInfo = "clients";
                                getPersonList();
                                break;
            case "Offres":      driveInfo = "offer";
                                getDriveList();
                                break;
            case "Demandes":    driveInfo = "appeal";
                                getDriveList();
                                break;
            case "Archive":     driveInfo = "done";    
                                getDoneList();
                                break;
            default:            info();        
        }

        session.setAttribute("message", message);
    }

    private void getPersonList()
    {
        filePath += personInfo + ".csv";
        PersonRW addr = new PersonRW(personInfo);
        message = addr.writeCSV(response, filePath);
        session.setAttribute("url", filePath);
    }

    private void getDriveList()
    {
        filePath += driveInfo + ".csv";
        DriveRW drv = new DriveRW(driveInfo);
        message = drv.writeCSV(response, filePath);
        session.setAttribute("url", "downloads/" + driveInfo + ".csv");
    }

    private void getDoneList()
    {
        filePath += "done.csv";
        DoneRW done = new DoneRW();
        message = done.writeCSV(response, filePath);
        session.setAttribute("url", "downloads/done.csv");
    }


}
