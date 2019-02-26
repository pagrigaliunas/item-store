package org.exercise.db;

import java.util.Map;
import org.exercise.service.model.Item;
import org.exercise.service.model.ItemLocationStock;

import java.util.List;
import org.exercise.service.model.Location;

public interface Repository
{
    void open();
    void close();

    Item readItem(int id);
    List<Item> readAllItems();

    void saveItem(Item item);
    void removeItem(int id);

    void removeItemLocation(Item item, ItemLocationStock itemLocationStock);
    void saveItemLocation(int itemId, ItemLocationStock itemLocationStock);
    Map<Integer, Location> getLocations();
}
