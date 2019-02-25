package org.exercise.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.exercise.db.Repository;
import org.exercise.service.model.Item;

import java.util.List;

public class ItemServiceImpl implements ItemService
{
    private static final Logger logger = LogManager.getLogger(ItemServiceImpl.class);

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
        logger.debug("Deleting Item with id " + id);
        repository.removeItem(id);
    }

    @Override
    public void updateItem(Item item)
    {

    }

    @Override
    public Item getItem(int id)
    {
        logger.debug("Getting Item with id " + id);
        return repository.readItem(id);
    }

    @Override
    public List<Item> getAllItems()
    {
        logger.debug("Getting all Items.");
        return repository.readAllItems();
    }
}
