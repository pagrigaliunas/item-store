package org.exercise.service.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Table;

@Embeddable
@Table(name="Locations")
public class GPS
{
    @Column
    private float log;
    @Column
    private float lat;

    public GPS()
    {
    }

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
