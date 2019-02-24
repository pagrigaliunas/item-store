package org.exercise.service;

import org.exercise.service.model.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemServiceImpl implements ItemService
{
    @Override
    public void addItem(Item item)
    {

    }

    @Override
    public void deleteItem(int id)
    {

    }

    @Override
    public void updateItem(Item item)
    {

    }

    @Override
    public Item getItem(int id)
    {
        return null;
//        Item item = new Item(id);
//        item.setTitle("Title");
//        item.setDescription("Item description");
//        item.setPrice(2.35f);
//        item.setStock(200);
//
//        Location location = new Location();
//        location.setCity("Kaunas");
//        location.setCountry("Lithuania");
//        location.setStreet("Didzioji");
//        location.setGps(new GPS(23.903597f, 54.898521f));
//        item.setLocation(location);
//
//        return item;
    }

    @Override
    public List<Item> getAllItems()
    {
        return new ArrayList<>();
    }
}
