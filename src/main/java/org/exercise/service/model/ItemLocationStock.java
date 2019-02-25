package org.exercise.service.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name="Items_Locations")
public class ItemLocationStock implements Serializable
{
    @Id
    @Column(name="item_id")
    int itemId;
    @Id
    @Column(name="location_id")
    int locationId;
    @Column
    private int stock;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="location_id")
    private Location location;

    @ManyToOne
    private Item item;

    public int getStock()
    {
        return stock;
    }

    public void setStock(int stock)
    {
        this.stock = stock;
    }

    public Location getLocation()
    {
        return location;
    }

    public void setLocation(Location location)
    {
        this.location = location;
    }
}
