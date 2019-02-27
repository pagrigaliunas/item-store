package org.exercise.rest;

import javax.validation.constraints.AssertTrue;
import javax.ws.rs.core.Response;
import org.exercise.service.model.Item;
import org.exercise.service.model.ItemLocationStock;
import org.exercise.service.model.Location;
import org.junit.Assert;
import org.junit.Test;

public class RestItemsServiceTest extends RestApiTest
{
    //TODO use config file
    private static final String BASE_URL = "http://localhost:9081/items";

    @Test
    public void getItemsTest()
    {
        RestAPIResponse<Item[]> response = sendGet(BASE_URL, Item[].class);

        Item[] items = response.getElement();
        Assert.assertNotNull(items);
        Assert.assertEquals(5, items.length);
    }

    @Test
    public void getSingleItemTest()
    {
        int id = 1;
        RestAPIResponse<Item> response = sendGet(BASE_URL + "/" + id, Item.class);

        Item item = response.getElement();
        Assert.assertNotNull(item);
        Assert.assertEquals(id, item.getId());
    }

    @Test
    public void failToGetItemTest()
    {
        int id = -1;
        RestAPIResponse<Item> response = sendGet(BASE_URL + "/" + id, Item.class);

        Assert.assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        Assert.assertNull(response.getElement());
    }

    @Test
    public void deleteSingleItemTest()
    {
        int id = 1;
        // check if item exists
        RestAPIResponse<Item> response = sendGet(BASE_URL + "/" + id, Item.class);
        Item item = response.getElement();
        Assert.assertNotNull(item);
        Assert.assertEquals(id, item.getId());

        // delete item
        RestAPIResponse<Object> deleteResponse = sendDelete(BASE_URL + "/" + id);
        Assert.assertEquals(Response.Status.OK.getStatusCode(), deleteResponse.getStatus());

        // check if item is deleted
        response = sendGet(BASE_URL + "/" + id, Item.class);
        Assert.assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        Assert.assertNull(response.getElement());
    }

    @Test
    public void addNewItemTest()
    {
        Item item = new Item();
        item.setTitle("Test 3D Printer");
        item.setDescription("Test 3D Printer Description");
        item.setPrice(23.5f);

        ItemLocationStock locationStock = new ItemLocationStock();
        locationStock.setStock(30);

        // location id should be enough to create new item.
        Location location = new Location();
        location.setId(1);
        locationStock.setLocation(location);
        item.getItemLocationStocks().add(locationStock);

        RestAPIResponse<Item> response = sendPost(BASE_URL, item, Item.class);
        Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        Item createdItem = response.getElement();
        Assert.assertNotNull(createdItem);

        Assert.assertTrue(createdItem.getId() > 0);
        Assert.assertEquals(item.getTitle(), createdItem.getTitle());
        Assert.assertEquals(item.getDescription(), createdItem.getDescription());
        Assert.assertEquals(item.getPrice(), createdItem.getPrice(), 0.0001f);

        Assert.assertEquals(locationStock.getStock(), createdItem.getTotalStock());
        Assert.assertEquals(1, createdItem.getItemLocationStocks().size());
        ItemLocationStock itemLocationStock = createdItem.getItemLocationStocks().get(0);
        Assert.assertEquals(locationStock.getStock(), itemLocationStock.getStock());

        Location createdItemLocation = itemLocationStock.getLocation();
        Assert.assertNotNull(createdItemLocation);
        Assert.assertEquals(location.getId(), createdItemLocation.getId());
    }
}
