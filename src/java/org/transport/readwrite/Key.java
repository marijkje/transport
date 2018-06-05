/* 
 * Copyright (C) 2017 Mael
 */
package org.transport.readwrite;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import javax.json.Json;
import javax.json.JsonException;
import javax.json.JsonObject;
import javax.json.JsonReader;

/**
 * class to fetch the google API key in the keys.json file
 * @author Mael
 */
public class Key 
{
    private final File file;
    
    /**
    * constructor creates File instance  
     */
    public Key()
    {
        String filePath;
        filePath = AdminRW.class.getResource("/resources/keys.json").getPath();
        this.file = new File(filePath);
    }
    
    /**
     * gets the key for the given API
     * @param name
     * @return string : "key=MYKEY"
     * @throws FileNotFoundException
     */
    public String get(String name) throws FileNotFoundException
    {
        InputStream is = new FileInputStream(this.file);
        JsonObject obj; 
        try { // Create JsonReader from Json.
                JsonReader reader = Json.createReader(is); 
                // Get the JsonObject structure from JsonReader.
                obj = reader.readObject();
            }  catch (JsonException e) {
                return null;
            }       
        JsonObject key = obj.getJsonObject("name");
        return key.getString(name).trim();
    }
    
}
