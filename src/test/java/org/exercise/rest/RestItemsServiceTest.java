package org.exercise.rest;

import javax.ws.rs.core.Response;

import com.github.fge.jsonpatch.JsonPatch;
import org.exercise.Config;
import org.exercise.service.model.Item;
import org.exercise.service.model.ItemLocationStock;
import org.exercise.service.model.Location;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class RestItemsServiceTest extends RestApiTest
{
    private static final String BASE_URL = "http://localhost:" + Config.getIntance().getPort() + "/items";

    @Test
    public void getItemsTest()
    {
        RestAPIResponse<Item[]> response = sendGet(BASE_URL, Item[].class);

        Item[] items = response.getElement();
        assertNotNull(items);
        assertEquals(5, items.length);
    }

    @Test
    public void getSingleItemTest()
    {
        int id = 1;
        RestAPIResponse<Item> response = sendGet(BASE_URL + "/" + id, Item.class);

        Item item = response.getElement();
        assertNotNull(item);
        assertEquals(id, item.getId());

        int total = item.getItemLocationStocks().stream().mapToInt(ItemLocationStock::getStock).sum();
        assertEquals(item.getTotalStock(), total);
    }

    @Test
    public void failToGetItemTest()
    {
        int id = -1;
        RestAPIResponse<Item> response = sendGet(BASE_URL + "/" + id, Item.class);

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertNull(response.getElement());
    }

    @Test
    public void deleteSingleItemTest()
    {
        int id = 1;
        // check if item exists
        RestAPIResponse<Item> response = sendGet(BASE_URL + "/" + id, Item.class);
        Item item = response.getElement();
        assertNotNull(item);
        assertEquals(id, item.getId());

        // delete item
        RestAPIResponse<Object> deleteResponse = sendDelete(BASE_URL + "/" + id);
        assertEquals(Response.Status.OK.getStatusCode(), deleteResponse.getStatus());

        // check if item is deleted
        response = sendGet(BASE_URL + "/" + id, Item.class);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertNull(response.getElement());
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
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        Item createdItem = response.getElement();
        assertNotNull(createdItem);

        assertTrue(createdItem.getId() > 0);
        assertEquals(item.getTitle(), createdItem.getTitle());
        assertEquals(item.getDescription(), createdItem.getDescription());
        assertEquals(item.getPrice(), createdItem.getPrice(), 0.0001f);

        assertEquals(locationStock.getStock(), createdItem.getTotalStock());
        assertEquals(1, createdItem.getItemLocationStocks().size());
        ItemLocationStock itemLocationStock = createdItem.getItemLocationStocks().get(0);
        assertEquals(locationStock.getStock(), itemLocationStock.getStock());

        Location createdItemLocation = itemLocationStock.getLocation();
        assertNotNull(createdItemLocation);
        assertEquals(location.getId(), createdItemLocation.getId());
    }

    @Test
    public void addNewItemWithInvalidStockAmountTest()
    {
        Item item = new Item();
        item.setTitle("Test 3D Printer");
        item.setDescription("Test 3D Printer Description");
        item.setPrice(23.5f);

        ItemLocationStock locationStock = new ItemLocationStock();
        // setting invalid stock amount
        locationStock.setStock(-30);

        // location id should be enough to create new item.
        Location location = new Location();
        location.setId(1);
        locationStock.setLocation(location);
        item.getItemLocationStocks().add(locationStock);

        RestAPIResponse<Object> response = sendPost(BASE_URL, item, null);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }

    @Test
    public void addNewItemWithInvalidLocationTest()
    {
        Item item = new Item();
        item.setTitle("Test 3D Printer");
        item.setDescription("Test 3D Printer Description");
        item.setPrice(23.5f);

        ItemLocationStock locationStock = new ItemLocationStock();
        locationStock.setStock(10);

        // location id should be enough to create new item.
        Location location = new Location();
        // setting invalid location id
        location.setId(-1);
        locationStock.setLocation(location);
        item.getItemLocationStocks().add(locationStock);

        RestAPIResponse<Object> response = sendPost(BASE_URL, item, null);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }


    @Test
    public void updateItemTest() throws Exception
    {
        int id = 1;
        RestAPIResponse<Item> response = sendGet(BASE_URL + "/" + id, Item.class);
        Item element = response.getElement();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertNotNull(element);

        String newTitle = "New 3d Printer";
        String newDescription = "Simple printer description";
        float price = 399.0f;
        int totalStock = 10;
        ItemLocationStock locationStockToRemove = element.getItemLocationStocks().get(0);

        String patchRow1 = "{\"op\": \"replace\", \"path\": \"/title\", \"value\": \"" + newTitle + "\"}";
        String patchRow2 = "{\"op\": \"replace\", \"path\": \"/description\", \"value\": \"" + newDescription + "\"}";
        String patchRow3 = "{\"op\": \"replace\", \"path\": \"/price\", \"value\": \"" + price + "\"}";
        String patchRow4 = "{\"op\": \"replace\", \"path\": \"/totalStock\", \"value\": \"" + totalStock + "\"}";
        String patchRow5 = "{\"op\": \"remove\", \"path\": \"/itemLocationStocks/0\"}";
        String patchStr = "[" + patchRow1 + "," + patchRow2 + "," + patchRow3 + "," + patchRow4 + "," + patchRow5 + "]";

        JsonPatch patch = getJsonMapper().readValue(patchStr, JsonPatch.class);

        response = sendPatch(BASE_URL + "/" + id, patch, Item.class);

        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        Item updatedElement = response.getElement();
        assertNotNull(updatedElement);

        assertEquals(newTitle, updatedElement.getTitle());
        assertEquals(newDescription, updatedElement.getDescription());
        assertEquals(price, updatedElement.getPrice(), 0.0001f);

        //totalStock should not change as it is calculated property
        assertEquals(element.getTotalStock() - locationStockToRemove.getStock(), updatedElement.getTotalStock());
        assertEquals(element.getItemLocationStocks().size() - 1, updatedElement.getItemLocationStocks().size());
    }

    @Test
    public void updateItemWithInvalidPriceTest() throws Exception
    {
        int id = 1;
        RestAPIResponse<Item> response = sendGet(BASE_URL + "/" + id, Item.class);
        Item element = response.getElement();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertNotNull(element);

        float price = -399.0f;

        String patchRow1 = "{\"op\": \"replace\", \"path\": \"/price\", \"value\": \"" + price + "\"}";
        String patchStr = "[" + patchRow1 + "]";

        JsonPatch patch = getJsonMapper().readValue(patchStr, JsonPatch.class);

        response = sendPatch(BASE_URL + "/" + id, patch, Item.class);

        assertNotNull(response);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }

    @Test
    public void updateItemWithInvalidLocationStockTest() throws Exception
    {
        int id = 1;
        RestAPIResponse<Item> response = sendGet(BASE_URL + "/" + id, Item.class);
        Item element = response.getElement();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertNotNull(element);

        int locationStock = -20;

        String patchRow1 = "{\"op\": \"replace\", \"path\": \"/itemLocationStocks/0/stock\", \"value\": \"" + locationStock + "\"}";
        String patchStr = "[" + patchRow1 + "]";

        JsonPatch patch = getJsonMapper().readValue(patchStr, JsonPatch.class);

        response = sendPatch(BASE_URL + "/" + id, patch, Item.class);

        assertNotNull(response);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }

    @Test
    public void updateItemWithInvalidLocationTest() throws Exception
    {
        int id = 1;
        RestAPIResponse<Item> response = sendGet(BASE_URL + "/" + id, Item.class);
        Item element = response.getElement();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertNotNull(element);

        int locationId = 0;

        //setting invalid location id
        String patchRow1 = "{\"op\": \"replace\", \"path\": \"/itemLocationStocks/0/location/id\", \"value\": \"" + locationId + "\"}";
        String patchStr = "[" + patchRow1 + "]";

        JsonPatch patch = getJsonMapper().readValue(patchStr, JsonPatch.class);

        response = sendPatch(BASE_URL + "/" + id, patch, Item.class);

        assertNotNull(response);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }
}
