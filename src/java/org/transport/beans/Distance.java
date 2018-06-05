/* 
 * Copyright (C) 2017 Mael
 */
package org.transport.beans;

import java.io.Serializable;

/**
 * Sets distance of this person from a certain person
 * Overrides compareTo method to sort distances from closest to farthest
 * 
 */
public class Distance implements Comparable<Distance>, Serializable{
    private Person person;
    private int meter;
    private int km;
    
    /**
     *
     * @param person
     */
    public void setPerson(Person person) {
        this.person = person;
    }

    public Person getPerson() {
        return this.person;
    }

    /**
     *
     * @param meter
     */
    public void setMeter(int meter) {
        this.meter = meter;
    }

    public int getMeter() {
        return this.meter;
    }
    
    public int getkm()
    {
        return this.meter/1000;
    }
/**
 * 
 * @return this person and distance as String
 */
    @Override
    public String toString() {
        return this.person + " (" + this.meter + ")";
    }

/**
 * 
 * Overrides compareTo method to sort distances from closest to farthest
 * @param driver
 */
    @Override
    public int compareTo(Distance driver) {
        return  this.meter - driver.meter;
    }
    
    
}
