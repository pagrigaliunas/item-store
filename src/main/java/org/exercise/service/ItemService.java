package org.exercise.service;

import org.exercise.service.model.Item;

import java.util.List;
import org.exercise.service.validation.ValidationException;

public interface ItemService
{
    void addItem(Item item) throws ValidationException;

    void deleteItem(int id);

    void updateItem(Item item) throws ValidationException;

    Item getItem(int id);

    List<Item> getAllItems();
}
