package org.exercise.service;

import org.exercise.db.Repository;
import org.exercise.service.model.Item;

import java.util.List;

public class ItemServiceImpl implements ItemService
{
    private Repository repository;

    public ItemServiceImpl(Repository repository)
    {
        this.repository = repository;
    }

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
        return repository.readItem(id);
    }

    @Override
    public List<Item> getAllItems()
    {
        return repository.readAllItems();
    }
}
