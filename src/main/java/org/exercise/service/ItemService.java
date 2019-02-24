package org.exercise.service;

import org.exercise.service.model.Item;

import java.util.List;

public interface ItemService
{
    void addItem(Item item);

    void deleteItem(Item item);

    void updateItem(Item item);

    Item getItem(int id);

    List<Item> getAllItems();
}
