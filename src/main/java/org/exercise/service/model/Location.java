package org.exercise.service.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="Locations")
public class Location
{
    @Id
    @GeneratedValue
    private int id;
    @Column
    private String country;
    @Column
    private String city;
    @Column
    private String street;
    private GPS gps;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getCountry()
    {
        return country;
    }

    public void setCountry(String country)
    {
        this.country = country;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public String getStreet()
    {
        return street;
    }

    public void setStreet(String street)
    {
        this.street = street;
    }

    public GPS getGps()
    {
        return gps;
    }

    public void setGps(GPS gps)
    {
        this.gps = gps;
    }
}