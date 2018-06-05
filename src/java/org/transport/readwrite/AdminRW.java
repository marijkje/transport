/* 
 * Copyright (C) 2017 Mael
 */
package org.transport.readwrite;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.transport.beans.User;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *  Read/write in users.xml file
 * 
 * @author Mael
 */
public class AdminRW
{
    private File file;
    
    public AdminRW() throws Exception
    {
        try {
            String filePath = AdminRW.class.getResource("/resources/users.xml").getPath();
            this.file = new File(filePath);
        }
        catch(Exception e) {
            throw new Exception("users.xml not found in /resources");
        }

    }
    
    /**
     *  returns a list of all users to Userform to create selectlist in users.jsp
     * 
     *  @return 
     */
    public List<User> readAll()
    {
        List<User> users = new ArrayList<>();
        
            final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            try 
            {
                final DocumentBuilder builder = factory.newDocumentBuilder();
                final Document document= builder.parse(file);
                final Element node = document.getDocumentElement();//users
                final NodeList nodes = node.getElementsByTagName("user");      //user 
                final int nb = nodes.getLength();
                for (int i = 0; i<nb; i++) 
                {
                    User user = new User();
                    NamedNodeMap usernode = nodes.item(i).getAttributes();
                    String roles = usernode.getNamedItem("roles").getNodeValue();
                    String motdepasse = usernode.getNamedItem("password").getNodeValue();
                    String nom = usernode.getNamedItem("username").getNodeValue();
                    user.setNom(nom);
                    user.setMotdepasse(motdepasse);
                    user.setRole(roles);
                    users.add(user);
                }
            }
            catch (final ParserConfigurationException | IOException e) {
                return null;
            }
            catch (org.xml.sax.SAXException ex)
            {
                return null;
            }
        return users;    
    }


/**
 * Adds a user to the users.xml file
 * 
 * @param user
 * @return message
 */
    public String write(User user)
    {
        String str = "Added user : ";
        
            final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            try 
            {
                final DocumentBuilder builder = factory.newDocumentBuilder();
                final Document document= builder.parse(file);
                final Element node = document.getDocumentElement();//users
                final NodeList nodes = node.getElementsByTagName("user");  //user 
                final int nb = nodes.getLength();
                Node child = nodes.item(nb-1).cloneNode(true);
                child.getAttributes().getNamedItem("username").setNodeValue(user.getNom());
                child.getAttributes().getNamedItem("password").setNodeValue(user.getMotdepasse());
                child.getAttributes().getNamedItem("roles").setNodeValue(user.getRole());
                node.appendChild(child);
                str += user.getNom();
                writeDocumentToFile(document, file);
            }
            catch (final ParserConfigurationException | IOException e) {
                return null;
            }
            catch (org.xml.sax.SAXException ex)
            {
                return null;
            }
        return str;    
        
    }

    /**
     *  Deletes a user from the xml file
     * 
     * @param user
     * @return message
     */
    public String delete(User user)
    {
        String str = "Deleted user : ";
        
            final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            try 
            {
                final DocumentBuilder builder = factory.newDocumentBuilder();
                final Document document= builder.parse(file);
                final Element node = document.getDocumentElement();//users
                final NodeList nodes = node.getElementsByTagName("user");      //user 
                final int nb = nodes.getLength();
                for (int i = 0; i<nb; i++) 
                {
                    NamedNodeMap usernode = nodes.item(i).getAttributes();
                    String nom = usernode.getNamedItem("username").getNodeValue();
                    if (nom.equals(user.getNom())) 
                    {
                        node.removeChild(nodes.item(i));
                        str += user.getNom();
                        break;
                    }
                }
                writeDocumentToFile(document, file);
            }
            catch (final ParserConfigurationException | IOException e) {
                return null;
            }
            catch (org.xml.sax.SAXException ex)
            {
                return null;
            }
        return str;    
        
    }

    /**
     *  Writes changed xml list 'document' to file users.xml
     * 
     * @param document
     * @param file 
     */
    private static void writeDocumentToFile(Document document, File file) {

        // Make a transformer factory to create the Transformer
        TransformerFactory tFactory = TransformerFactory.newInstance();

        // Make the Transformer
        Transformer transformer;
        try
        {
            transformer = tFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");        
            // Mark the document as a DOM (XML) source
            DOMSource source = new DOMSource(document);

            // Say where we want the XML to go
            StreamResult result = new StreamResult(file);

                // Write the XML to file
            transformer.transform(source, result);
        }
        catch (TransformerException ex)
        {
            Logger.getLogger(AdminRW.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
    /**
     *  Compares user (name, pass and role) with list retrieved from xml file
     * 
     * @param name
     * @param pass
     * @param role
     * @return true if user exists in file
     */
    public boolean isAllowed(String name, String pass, String role)
    {
        boolean OK = false;
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try 
        {
            final DocumentBuilder builder = factory.newDocumentBuilder();
            final Document document= builder.parse(file);
            final Element node = document.getDocumentElement();//users
            final NodeList nodes = node.getElementsByTagName("user");      //user 
            final int nb = nodes.getLength();
            for (int i = 0; i<nb; i++) 
            {
                NamedNodeMap usernode = nodes.item(i).getAttributes();
                String roles = usernode.getNamedItem("roles").getNodeValue();
                String motdepasse = usernode.getNamedItem("password").getNodeValue();
                String nom = usernode.getNamedItem("username").getNodeValue();
                if (roles.contains(role)) 
                {
                    if (name.equals(nom) && pass.equals(motdepasse)) OK = true;
                }
            }
        }
        catch (final ParserConfigurationException | IOException e){}
        catch (org.xml.sax.SAXException ex){}
        return OK;
    }
    
    
}
