/* 
 * Copyright (C) 2017 Mael
 */
package org.transport.beans;

import java.io.Serializable;

/**
 *  Gets and sets values for availability of drivers
 *  begin and end hours : (7,19) as default
 * @author Mael
 */
public class Dispo  implements Serializable
{
    private String lundi;
    private String mardi;
    private String mercredi;
    private String jeudi;
    private String vendredi;
    private String samedi;
    private String dimanche;
    private String jour;
    
    public String getJour()
    {
        switch(this.jour) 
        {
            case "dimanche" :   return getDimanche();
            case "lundi" :      return getLundi();
            case "mardi" :      return getMardi();
            case "mercredi" :   return getMercredi();
            case "jeudi" :      return getJeudi();
            case "vendredi" :   return getVendredi();
            case "samedi" :     return getSamedi();
            default :           return this.jour;
        }
    }
    
    public String getDimanche()
    {
        return dimanche;
    }
    
    public String getLundi()
    {
        return lundi;
    }
    
    public String getMardi()
    {
        return mardi;
    }
    
    public String getMercredi()
    {
        return mercredi;
    }
    
    public String getJeudi()
    {
        return jeudi;
    }
    
    public String getVendredi()
    {
        return vendredi;
    }
    
    public String getSamedi()
    {
        return samedi;
    }
    
    public void setJour(String jour)
    {
        this.jour = jour;
    }
    
    public void setDimanche(String dimanche)
    {
        if (dimanche == null) this.dimanche = "7,19";
        else this.dimanche = dimanche;
    }
    
    public void setLundi(String lundi)
    {
        if (lundi == null) this.lundi = "7,19";
        else this.lundi = lundi;
    }
    
    public void setMardi(String mardi)
    {
        if (mardi == null) this.mardi = "7,19";
        else this.mardi = mardi;
    }
    
    public void setMercredi(String mercredi)
    {
        if (mercredi == null) this.mercredi = "7,19";
        else this.mercredi = mercredi;
    }
    
    public void setJeudi(String jeudi)
    {
        if (jeudi == null) this.jeudi = "7,19";
        else this.jeudi = jeudi;
    }
    
    public void setVendredi(String vendredi)
    {
        if (vendredi == null) this.vendredi = "7,19";
        else this.vendredi = vendredi;
    }
    
    public void setSamedi(String samedi)
    {
        if (samedi == null) this.samedi = "7,19";
        else this.samedi = samedi;
    }
    
    
}
