package org.exercise.db;

import org.exercise.service.model.Item;
import org.exercise.service.model.ItemLocation;

import java.util.List;

public interface Repository
{
    void init();
    void close();

    Item readItem(int id);
    List<Item> readAllItems();

    void saveItem(Item item);
    void saveItemLocation(int itemId, ItemLocation itemLocation);

    void removeItem(Item item);
    void removeItemLocation(Item item, ItemLocation itemLocation);
}
