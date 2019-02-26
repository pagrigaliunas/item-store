package org.exercise.db.h2;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.exercise.db.Repository;
import org.exercise.service.model.GPS;
import org.exercise.service.model.Item;
import org.exercise.service.model.ItemLocationStock;
import org.exercise.service.model.Location;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class H2Repository implements Repository
{
    private static final Logger logger = LogManager.getLogger(H2Repository.class);

    private Connection connection;
    private Map<Integer, Location> locations = new HashMap<>();

    @Override
    public void open()
    {
        logger.info("Initializing H2 Db repository...");
        try
        {
            createDbStructure();
            loadLocations();
            logger.info("H2 Db repository initialized.");
        }
        catch (SQLException exc)
        {
            logger.error("Failed to initialize H2 Db repository.", exc);
        }
    }

    @Override
    public void close()
    {
        try
        {
            if (connection != null)
            {
                connection.close();
            }
        }
        catch (SQLException exc)
        {
            logger.error("Failed to close connection.", exc);
        }
    }

    @Override
    public Item readItem(int id)
    {
        try(PreparedStatement statement = connection.prepareStatement(
                " SELECT *, location_id, stock FROM Items i " +
                " LEFT JOIN Items_Locations il ON i.id = il.item_id " +
                " WHERE i.id = ? "))
        {
            statement.setInt(1, id);
            try(ResultSet resultSet = statement.executeQuery())
            {

                List<Item> items = createItems(resultSet);
                if (!items.isEmpty())
                {
                    return items.get(0);
                }
            }
        }
        catch (SQLException exc)
        {
            logger.error("Failed to read Item.", exc);
        }
        return null;
    }

    @Override
    public List<Item> readAllItems()
    {
        try(Statement statement = connection.createStatement())
        {
            try(ResultSet resultSet = statement.executeQuery(
                    " SELECT *, location_id, stock FROM Items i " +
                    " LEFT JOIN Items_Locations il ON i.id = il.item_id " +
                    " ORDER BY i.id"))
            {
                return createItems(resultSet);
            }
        }
        catch (SQLException exc)
        {
            logger.error("Failed to read all Item.", exc);
        }
        return Collections.emptyList();
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
            logger.error("Failed to save Item.", exc);
        }
    }

    @Override
    public void saveItemLocation(int itemId, ItemLocationStock itemLocationStock)
    {

    }

    @Override
    public void removeItem(int id)
    {
        try(PreparedStatement statement = connection.prepareStatement("DELETE FROM Items WHERE id = ?"))
        {
            statement.setInt(1, id);
            statement.execute();
        }
        catch (SQLException exc)
        {
            logger.error("Failed to delete Item.", exc);
        }
    }

    @Override
    public void removeItemLocation(Item item, ItemLocationStock itemLocationStock)
    {
        try(PreparedStatement statement = connection.prepareStatement("DELETE FROM Items_Locations WHERE item_id = ? AND location_id = ?"))
        {
            statement.setInt(1, item.getId());
            statement.setInt(1, itemLocationStock.getLocation().getId());
            statement.execute();
        }
        catch (SQLException exc)
        {
            logger.error("Failed to delete Item Location.", exc);
        }
    }

    @Override
    public Map<Integer, Location> getLocations()
    {
        return locations;
    }

    private void addItem(Item item) throws SQLException
    {
        logger.debug("Adding new Item...");
        connection.setAutoCommit(false);
        try(PreparedStatement statement = connection.prepareStatement(
                " INSERT INTO Items(title, description, price) " +
                " VALUES(?, ?, ?)", Statement.RETURN_GENERATED_KEYS))
        {
            statement.setString(1, item.getTitle());
            statement.setString(2, item.getDescription());
            statement.setFloat(3, item.getPrice());
            statement.execute();

            try(ResultSet generatedKeys = statement.getGeneratedKeys())
            {
                if (generatedKeys.next())
                {
                    item.setId(generatedKeys.getInt(1));
                    addItemQuantitiesPerLocation(item);
                    connection.commit();
                } else
                {
                    logger.warn("Failed to generated Item id. Roll backing changes");
                    connection.rollback();
                }
            }
        }
        catch (SQLException exc)
        {
            logger.error("Failed to add Item", exc);
            connection.rollback();
        }
        finally
        {
            connection.setAutoCommit(true);
        }
        logger.debug("Added new Item.");
    }

    private void addItemQuantitiesPerLocation(Item item) throws SQLException
    {
        List<ItemLocationStock> itemStockList = item.getItemLocationStocks();
        if (!itemStockList.isEmpty())
        {
            try(PreparedStatement statement = connection.prepareStatement("INSERT INTO Items_Locations VALUES(?, ?, ?)"))
            {
                for (ItemLocationStock locationStock : itemStockList)
                {
                    statement.setInt(1, item.getId());
                    statement.setInt(2, locationStock.getLocation().getId());
                    statement.setInt(3, locationStock.getStock());
                    statement.addBatch();
                }

                statement.executeBatch();
            }
        }
    }

    private List<Item> createItems(ResultSet resultSet) throws SQLException
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

    private void createDbStructure() throws SQLException {
        logger.info("Creating db structure ...");
        String url = "jdbc:h2:mem:store";
        ClassLoader classLoader = getClass().getClassLoader();
        URL fileUrl = classLoader.getResource("sql/h2_schema.sql");
        if (fileUrl != null)
        {
            url += ";INIT=runscript from '" + fileUrl +"'";
        }

        connection = DriverManager.getConnection(url, "sa", "");
        logger.info("Db structure created.");
    }

    private void loadLocations()
    {
        try
        {
            logger.debug("Loading all locations...");
            try(Statement statement = connection.createStatement())
            {
                try(ResultSet resultSet = statement.executeQuery("SELECT * FROM Locations"))
                {
                    while (resultSet.next())
                    {
                        Location location = new Location();
                        location.setId(resultSet.getInt(1));
                        location.setCountry(resultSet.getString(2));
                        location.setCity(resultSet.getString(3));
                        location.setStreet(resultSet.getString(4));

                        GPS gps = new GPS(resultSet.getFloat(5), resultSet.getFloat(6));
                        location.setGps(gps);
                        locations.put(location.getId(), location);
                    }
                }
            }
            logger.debug("Number of loaded Locations: {}.", locations.size());
        }
        catch (SQLException exc)
        {
            logger.error("Failed to load locations.", exc);
        }
    }
}
