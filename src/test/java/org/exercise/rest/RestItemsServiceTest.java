package org.exercise.rest;

import javax.ws.rs.core.Response;
import org.exercise.service.model.Item;
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
        RestAPIResponse<Item> response = sendGet(BASE_URL + "/" + id, Item.class);
        Item item = response.getElement();
        Assert.assertNotNull(item);
        Assert.assertEquals(id, item.getId());

        RestAPIResponse<Object> deleteResponse = sendDelete(BASE_URL + "/" + id);
        Assert.assertEquals(Response.Status.OK.getStatusCode(), deleteResponse.getStatus());

        response = sendGet(BASE_URL + "/" + id, Item.class);
        Assert.assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        Assert.assertNull(response.getElement());
    }
}
