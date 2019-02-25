package org.exercise.db;

import org.exercise.service.model.Item;
import org.exercise.service.model.ItemLocationStock;

import java.util.List;

public interface Repository
{
    void open();
    void close();

    Item readItem(int id);
    List<Item> readAllItems();

    void saveItem(Item item);
    void saveItemLocation(int itemId, ItemLocationStock itemLocationStock);

    void removeItem(int id);
    void removeItemLocation(Item item, ItemLocationStock itemLocationStock);
}
