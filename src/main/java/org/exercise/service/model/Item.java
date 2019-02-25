package org.exercise.service.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="Items")
public class Item
{
    @Id
    @GeneratedValue
    private int id;
    @Column
    private String title;
    @Column
    private String description;
    @Column
    private float price;
    @Transient
    private int totalStock;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name="item_id")
    private List<ItemLocationStock> itemLocationStocks = new ArrayList<>();

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

    public List<ItemLocationStock> getItemLocationStocks()
    {
        return itemLocationStocks;
    }

    public void setItemLocationStocks(List<ItemLocationStock> itemLocationStocks)
    {
        this.itemLocationStocks = itemLocationStocks;
    }
}
