package org.exercise.service;

import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.exercise.db.Repository;
import org.exercise.service.model.Item;

import java.util.List;
import org.exercise.service.validation.ValidationException;

public class ItemServiceImpl implements ItemService
{
    private static final Logger logger = LogManager.getLogger(ItemServiceImpl.class);

    private final Repository repository;

    private List<Item> items;
    private volatile boolean dirty;
    private final ReentrantReadWriteLock lock;

    public ItemServiceImpl(Repository repository)
    {
        this.repository = repository;
        lock = new ReentrantReadWriteLock();
        items = repository.readAllItems();
    }

    @Override
    public void addItem(Item item) throws ValidationException
    {
        //TODO validate item;

        lock.writeLock().lock();
        repository.saveItem(item);
        dirty = true;
        lock.writeLock().unlock();
    }

    @Override
    public void deleteItem(int id)
    {
        logger.debug("Deleting Item with id " + id);
        lock.writeLock().lock();
        repository.removeItem(id);
        dirty = true;
        lock.writeLock().unlock();
    }

    @Override
    public void updateItem(Item item) throws ValidationException
    {
        //TODO validate item;

        lock.writeLock().lock();
        repository.saveItem(item);
        dirty = true;
        lock.writeLock().unlock();
    }

    @Override
    public Item getItem(int id)
    {
        logger.debug("Getting Item with id " + id);
        refreshItemData();
        return findItem(id);
    }

    @Override
    public List<Item> getAllItems()
    {
        logger.debug("Getting all Items.");
        refreshItemData();
        return items;
    }

    private Item findItem(int id)
    {
        for (Item item : items)
        {
            if (item.getId() == id)
            {
                return item;
            }
        }
        return null;
    }

    private void refreshItemData()
    {
        lock.readLock().lock();
        if (dirty)
        {
            lock.readLock().unlock();
            lock.writeLock().lock();
            // we still need to check if other read thread might already refreshed it.
            // In this case no need for pointless cache refresh.
            if (dirty)
            {
                items = repository.readAllItems();
                dirty = false;
            }
            lock.readLock().lock();
            lock.writeLock().unlock();
        }
        lock.readLock().unlock();
    }
}
