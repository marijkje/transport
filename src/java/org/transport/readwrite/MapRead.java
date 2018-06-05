/* 
 * Copyright (C) 2017 Mael
 */
package org.transport.readwrite;

import org.transport.beans.Distance;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
 
import javax.json.Json;
import javax.json.JsonException;
import javax.json.JsonObject;
import javax.json.JsonReader;
import org.transport.beans.Person;
import org.transport.beans.Drive;
 
/**
 * Reads the distances between the given person and all the matching persons
 * sorts the list from closest farthest matching person 
 *
 * @author Mael
 */
public class MapRead 
{
    private InputStream is;
    private final List<Distance> personsDistances;
    private final Key key;
    
    /**
     * 
     * Performs all google requests
     * And adapts data to and from google
     */
    public MapRead()
    {
        this.personsDistances = new ArrayList<>();
        this.key = new Key();
    }
    
    /**
     * 
     * Called from method showAddress in Addressform class
     * Creates distanceUrl for google API distancematrix,
     * gets stream from url,
     * puts the information from stream in the personsDistances list,
     * @param drive
     * @param persons
     * @return message
     */
    public String read(Drive drive, List<Person> persons)
    {
        URL url;
        String distanceUrl = "";
        try {              
            distanceUrl += "https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + drive.googleDepart() 
                    + "&destinations=" + googleList(persons) + "&" + key.get("distance");
            url = new URL(distanceUrl);
            this.is = url.openStream();
        } catch (FileNotFoundException ex) {
            return "Error reading key : " + ex;
        } catch (MalformedURLException ex)
        {
            return "Malformed Url : " + ex;
        } catch (IOException ex)
        {
            return "Error opening stream : " + ex;
        }
        
        JsonObject obj; 
        try { // Create JsonReader from Json.
                JsonReader reader = Json.createReader(this.is); 
                obj = reader.readObject();
                is.close();
        } catch (JsonException e) {
            return "Error json reader : " + e;
        } catch(IOException e){
            return "Error closing stream : " + e;
        }
        JsonObject rows = null;
        if (obj != null)
            rows = obj.getJsonArray("rows").getJsonObject(0);
        
        if (rows!=null)
        {
            int i = 0;
            int d;
            for (Person pers : persons)
            {
                Distance dist = new Distance();
                String status = rows.getJsonArray("elements").getJsonObject(i).getJsonString("status").toString();
                if (status.contains("OK"))
                {
                    d = rows.getJsonArray("elements").getJsonObject(i).getJsonObject("distance").getInt("value");
                    dist.setPerson(pers);
                    dist.setMeter(d);
                    this.personsDistances.add(dist);
                }
                i++;
            }
            return "";
        }
        return distanceUrl;//"Erreur, aucun chauffeur trouvé";
    }
    
    /**
     *  Sorts the personsDistances list from closest to farthest
     *  @return the list with distances from the persons to a person
     */
    public List<Distance> getDistances()
    {
        Collections.sort(this.personsDistances);
        return this.personsDistances;
    }

     /**
     *  Adapts the persons addresses list for request url
     * @param persons
     *  @return the string ready to use
     */
    public String googleList(List<Person> persons)
    {
        String s = "";
        int i = 0;
        for (Person person : persons) 
        {
            if (i++ > 0) s += "|";
            s += person.googleAddress();
        }
        return s;
    }
    
     /**
     *  retrieves a map around the persons address 
     * 
     * @param address
     * @param size
     *  @return the string ready to use
     */
    public String getMap(String address, String size) throws Exception {
        if (address != null)
        {
            String url = "https://maps.googleapis.com/maps/api/staticmap?";
            url += address;
            url += "&size=" + size; 
            url += "&zoom=" + 11;
            url += "&maptype=roadmap";
            url += "&sensor=false";
            url += "&" + this.key.get("static");
//            url += "&" + this.key.get("new");
          return url;
        }
        return null;
    }
    
    
    
     /**
     *  Returns total number of kilometers traveled by the driver
     * 
     * @param drive
     * @param person
     * @param driveInfo
     *  @return integer kiloMeters
     */
    public int getKilometers(Drive drive, Person person, String driveInfo)
    {
        URL url;
        String distanceUrl = "";
        String status;
        int distance = 0;

        try {              
            distanceUrl += "https://maps.googleapis.com/maps/api/directions/json?" 
                    + drive.googleAddresses(person, driveInfo) + 
                     "&" + key.get("direction");
            url = new URL(distanceUrl);
            this.is = url.openStream();
        } catch (FileNotFoundException ex) {
            return 0;
        } catch (MalformedURLException ex)
        {
            return 0;
        } catch (IOException ex)
        {
            return 0;
        }
        
        JsonObject obj; 
        try { // Create JsonReader from Json.
                JsonReader reader = Json.createReader(this.is); 
                obj = reader.readObject();
                is.close();
        } catch (JsonException | IOException e) {
            return 0;
        }
        JsonObject rows;
        if (obj != null)
        {
            status =obj.getJsonString("status").toString();
            if (status.contains("OK"))
            {    
                rows = obj.getJsonArray("routes").getJsonObject(0);
                
                for (int i=0; i<rows.getJsonArray("legs").size(); i++ )
                {
                    distance += rows.getJsonArray("legs").getJsonObject(i).getJsonObject("distance").getInt("value");
                }
            }
        }
        return distance / 1000;
    }
 
    
}
