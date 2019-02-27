package org.exercise.rest;

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
        Item[] items = sendGet(BASE_URL, Item[].class);

        Assert.assertNotNull(items);
        Assert.assertEquals(5, items.length);
    }

    @Test
    public void getSingleItemTest()
    {
        int id = 1;
        Item item = sendGet(BASE_URL + "/" + id, Item.class);

        Assert.assertNotNull(item);
        Assert.assertEquals(id, item.getId());
    }

    @Test
    public void getFailToGetItemTest()
    {
        int id = -1;
        Item item = sendGet(BASE_URL + "/" + id, Item.class);

        Assert.assertNull(item);
    }
}
