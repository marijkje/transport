/* 
 * Copyright (C) 2017 Mael
 */
package org.transport.readwrite;

import com.sun.mail.util.MailSSLSocketFactory;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
/**
 *
 * @author Mael
 */
public class Email
{
    private final String from;
    private final String host;
    
    private final MimeMessage message;
    private Session session;
    private Transport tr;
    

    public Email()
    {
        from = "transportsolidaire07@gmail.com";
        host = "smtp.gmail.com";
        session = null;
        tr = null;
        
        Properties props = System.getProperties();
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.user", "transportsolidaire07");
        props.put("mail.smtp.password", "Solidaire07360");
        props.put("mail.smtp.port", "587");
        try {
            MailSSLSocketFactory sf;
            sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true); 
            props.put("mail.smtp.ssl.trust", "*");
            props.put("mail.smtp.ssl.socketFactory", sf);
        } catch (GeneralSecurityException ex) {
            Logger.getLogger(Email.class.getName()).log(Level.SEVERE, null, ex);
        }
        // creates a new session with an authenticator
        Authenticator auth;
        auth = new Authenticator() {
            @Override
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("Solidaire07360", "Solidaire07360");
            }
        };
 
        session = Session.getInstance(props, auth);
        message = new MimeMessage(session);  
    }
    

    /**
     *
     * @param toMails addresses to send mail to
     * @param subject subject of the mail
     * @param text  the actual message
     * @return information string
     */

    
    public String send(List<String>toMails, String subject, String text) 
    {
        try {
            message.setFrom(new InternetAddress(from));
            message.setSubject(subject);
            message.setText(text, "UTF-8", "html");
            
            for (String toMail : toMails)
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(toMail));
            
            if (message.getAllRecipients() == null) return "Pas d'adresses mail";
            tr = session.getTransport("smtp");
            tr.connect(host, "transportsolidaire07", "Solidaire07360");
            tr.sendMessage(message, message.getAllRecipients());
            tr.close();
        } catch (MessagingException mex) 
        {
        return mex.toString();
        }
   
    return "Message envoyée ";
    }
}