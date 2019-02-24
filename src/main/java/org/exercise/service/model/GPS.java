package org.exercise.service.model;

public class GPS
{
    private float log;
    private float lat;

    public GPS(float log, float lat)
    {
        this.lat = lat;
        this.log = log;
    }

    public float getLog()
    {
        return log;
    }

    public void setLog(float log)
    {
        this.log = log;
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
