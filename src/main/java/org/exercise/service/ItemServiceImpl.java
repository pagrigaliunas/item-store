package org.exercise.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.exercise.db.Repository;
import org.exercise.service.model.Item;

import org.exercise.service.model.ItemLocationStock;
import org.exercise.service.model.Location;
import org.exercise.service.validation.ItemValidator;
import org.exercise.service.validation.ValidationException;

public class ItemServiceImpl implements ItemService
{
    private static final Logger logger = LogManager.getLogger(ItemServiceImpl.class);

    private final Repository repository;
    private Map<Integer, Item> items;
    private volatile boolean dirty = true;
    private final ReentrantReadWriteLock lock;

    public ItemServiceImpl(Repository repository)
    {
        this.repository = repository;
        lock = new ReentrantReadWriteLock();
    }

    @Override
    public void addItem(Item item) throws ValidationException
    {
        ItemValidator validator = new ItemValidator(repository.getLocations());
        validator.validate(item);

        lock.writeLock().lock();
        // clearing id so new item will be created in db.
        item.setId(0);
        repository.saveItem(item);
        int total = item.getItemLocationStocks().stream().mapToInt(ItemLocationStock::getStock).sum();
        item.setTotalStock(total);

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
        ItemValidator validator = new ItemValidator(repository.getLocations());
        validator.validate(item);

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
        return items.get(id);
    }

    @Override
    public Collection<Item> getAllItems()
    {
        logger.debug("Getting all Items.");
        refreshItemData();
        return items.values();
    }

    @Override
    public Collection<Location> getAllLocations()
    {
        return repository.getLocations().values();
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
                List<Item> items = repository.readAllItems();
                items.forEach(i ->
                {
                    int total = i.getItemLocationStocks().stream().mapToInt(ItemLocationStock::getStock).sum();
                    i.setTotalStock(total);
                });
                this.items = items.stream().collect(Collectors.toMap(Item::getId, item -> item));
                dirty = false;
            }
            lock.readLock().lock();
            lock.writeLock().unlock();
        }
        lock.readLock().unlock();
    }
}
