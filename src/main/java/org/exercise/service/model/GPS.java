package org.exercise.service.model;

public class GPS
{
    private float lon;
    private float lat;

    public GPS()
    {
    }

    public GPS(float lon, float lat)
    {
        this.lat = lat;
        this.lon = lon;
    }

    public float getLon()
    {
        return lon;
    }

    public void setLon(float lon)
    {
        this.lon = lon;
    }

    public float getLat()
    {
        return lat;
    }

    public void setLat(float lat)
    {
        this.lat = lat;
    }
}
