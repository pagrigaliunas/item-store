package org.exercise.service;

import java.util.Collection;
import org.exercise.service.model.Item;

import org.exercise.service.model.Location;
import org.exercise.service.validation.ValidationException;

public interface ItemService
{
    void addItem(Item item) throws ValidationException;

    void deleteItem(int id);

    void updateItem(Item item) throws ValidationException;

    Item getItem(int id);

    Collection<Item> getAllItems();

    Collection<Location> getAllLocations();
}
