package org.exercise.db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.exercise.service.model.GPS;
import org.exercise.service.model.Item;
import org.exercise.service.model.ItemLocationStock;
import org.exercise.service.model.Location;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public abstract class AbstractRepository implements Repository
{
//    public static final String ENTITY_PACKAGE_KEY = "entity.package";
    private static final Logger logger = LogManager.getLogger(AbstractRepository.class);

    private Connection connection;
    private SessionFactory sessionFactory;
    private Map<Integer, Location> locations = new HashMap<>();

    @Override
    public void open()
    {
        logger.info("Initializing Db repository...");
        createDbStructure();
        loadLocations();
        logger.info("Db repository initialized.");
    }

    @Override
    public void close()
    {
        if (sessionFactory != null)
        {
            sessionFactory.close();
        }
        logger.debug("Repository closed.");
    }

    @Override
    public List<Item> readAllItems()
    {
        try (Session session = sessionFactory.openSession())
        {
            return session.createQuery("From Item", Item.class).list();
        }
    }

    @Override
    public Item readItem(int id)
    {
        try (Session session = sessionFactory.openSession())
        {
            return session.get(Item.class, id);
        }
    }

    @Override
    public void removeItem(int id)
    {
        try (Session session = sessionFactory.openSession())
        {
            Transaction transaction = session.beginTransaction();
            Item item = new Item();
            item.setId(id);
            session.delete(item);
            transaction.commit();
        }
    }

    protected abstract Properties getDbConfiguration();

    private void createDbStructure()
    {
        logger.info("Creating Db structure ...");
        getSessionFactory(getDbConfiguration());
        logger.info("Db structure created.");
    }

    private void getSessionFactory(Properties properties)
    {
        if (sessionFactory == null)
        {
            logger.debug("Creating Hibernate session factory...");

            Configuration cfg = new Configuration();
            cfg.setProperties(properties);
//            cfg.addPackage(properties.getProperty(ENTITY_PACKAGE_KEY));
            cfg.addAnnotatedClass(Item.class);
            cfg.addAnnotatedClass(ItemLocationStock.class);
            cfg.addAnnotatedClass(Location.class);
            cfg.addAnnotatedClass(GPS.class);
            sessionFactory = cfg.buildSessionFactory();

            logger.debug("Created Hibernate session factory.");
        }
        else
        {
            logger.debug("Hibernate session factory already initialized.");
        }
    }

    private void loadLocations()
    {
        logger.debug("Loading all locations...");
        try (Session session = sessionFactory.openSession())
        {
            List<Location> locationList = session.createQuery("FROM Location", Location.class).list();
            locationList.forEach(location -> locations.put(location.getId(), location));
        }
        logger.debug("Number of loaded Locations: {}.", locations.size());
    }

    @Override
    public void saveItem(Item item)
    {
        try
        {
            if (item.getId() <= 0)
            {
                addItem(item);
            }
            else
            {

            }
        }
        catch (SQLException exc)
        {
            logger.error("Failed to save Item.");
        }
    }

    @Override
    public void saveItemLocation(int itemId, ItemLocationStock itemLocationStock)
    {

    }

    @Override
    public void removeItemLocation(Item item, ItemLocationStock itemLocationStock)
    {
        try
        {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM Items_Locations WHERE item_id = ? AND location_id = ?");
            statement.setInt(1, item.getId());
            statement.setInt(1, itemLocationStock.getLocation().getId());
            statement.execute();
        }
        catch (SQLException exc)
        {
            logger.error("Failed to delete Item Location.", exc);
        }
    }

    private void addItem(Item item) throws SQLException
    {
        logger.debug("Adding new Item...");
        PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO Items(title, description, price) " +
                " VALUES(?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, item.getTitle());
        statement.setString(2, item.getDescription());
        statement.setFloat(3, item.getPrice());
        statement.addBatch();
        addItemLocations(item);
        statement.executeBatch();

        ResultSet generatedKeys = statement.getGeneratedKeys();
        if (generatedKeys.next())
        {
            item.setId(generatedKeys.getInt(1));
        }
        logger.debug("Added new Item.");
    }

    private void addItemLocations(Item item) throws SQLException
    {
        logger.debug("Adding Item Locations...");
        PreparedStatement statement = connection.prepareStatement("INSERT INTO Items_Locations VALUES(?, ?, ?)");
        for(ItemLocationStock location : item.getItemLocationStocks())
        {
            statement.setInt(1, item.getId());
            statement.setInt(2, location.getLocation().getId());
            statement.setInt(3, location.getStock());
            statement.addBatch();
        }
    }

    private List<Item> createItemsFromResultSet(ResultSet resultSet) throws SQLException
    {
        List<Item> items = new ArrayList<>();
        Item item = new Item();
        logger.debug("Loading Items...");
        while(resultSet.next())
        {
            int id = resultSet.getInt(1);
            if (item.getId() != id)
            {
                item = new Item();
                item.setId(id);
                item.setTitle(resultSet.getString(2));
                item.setDescription(resultSet.getString(3));
                item.setPrice(resultSet.getFloat(4));
                items.add(item);
            }

            Location location = locations.get(resultSet.getInt("location_id"));
            if (location != null)
            {
                ItemLocationStock itemLocationStock = new ItemLocationStock();
                itemLocationStock.setLocation(location);
                itemLocationStock.setStock(resultSet.getInt("stock"));
                item.getItemLocationStocks().add(itemLocationStock);
                item.setTotalStock(item.getTotalStock() + itemLocationStock.getStock());
            }
        }
        logger.debug("Number of loaded Items: {}.", items.size());
        return items;
    }
}
