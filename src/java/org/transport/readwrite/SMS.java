/* 
 * Copyright (C) 2017 Mael
 */
package org.transport.readwrite;


import java.net.*;

public class SMS {

	/**
	 * 
	 * 
	 */
    public SMS()
    {
    }
        
    public String send(String number)
    {
        String requestUrl = "";
	String recipient = number;
	String message = "Hello Charly";
	String username = "admin";
	String password = "admin07";
	String originator = "0686637525";
		
	
	try 
        {
            requestUrl  += "http://127.0.0.1:9501/api?action=sendmessage&" +
            "username=" + URLEncoder.encode(username, "UTF-8") +
            "&password=" + URLEncoder.encode(password, "UTF-8") + 
            "&recipient=" + URLEncoder.encode(recipient, "UTF-8") + 
            "&messagetype=SMS:TEXT" +
            "&messagedata=" + URLEncoder.encode(message, "UTF-8") +
            "&originator=" + URLEncoder.encode(originator, "UTF-8") +
            "&serviceprovider=GSMModem1" +
            "&responseformat=html";
			
			
			
            URL url = new URL(requestUrl);		  
            HttpURLConnection uc = (HttpURLConnection)url.openConnection();
		  
            System.out.println(uc.getResponseMessage());

            uc.disconnect();
		  
		  
	} catch(Exception ex) 
        {
            System.out.println(ex.getMessage());
			
	}
        return requestUrl;
    }

}
