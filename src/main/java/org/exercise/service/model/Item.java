package org.exercise.service.model;

import java.util.ArrayList;
import java.util.List;

public class Item
{
    private int id;
    private String title;
    private String description;
    private float price;
    private int totalStock;
    private List<ItemLocation> itemLocations = new ArrayList<>();

    public void setId(int id)
    {
        this.id = id;
    }

    public int getId()
    {
        return id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public float getPrice()
    {
        return price;
    }

    public void setPrice(float price)
    {
        this.price = price;
    }

    public int getTotalStock()
    {
        return totalStock;
    }

    public void setTotalStock(int totalStock)
    {
        this.totalStock = totalStock;
    }

    public List<ItemLocation> getItemLocations()
    {
        return itemLocations;
    }

    public void setItemLocations(List<ItemLocation> itemLocations)
    {
        this.itemLocations = itemLocations;
    }
}
