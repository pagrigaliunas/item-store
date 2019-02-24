package org.exercise.service.model;

public class Location
{
    private int id;
    private String country;
    private String city;
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