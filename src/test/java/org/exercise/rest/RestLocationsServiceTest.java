package org.exercise.rest;

import org.exercise.service.model.Location;
import org.junit.Assert;
import org.junit.Test;

public class RestLocationsServiceTest extends RestApiTest
{
    //TODO use config file
    private static final String BASE_URL = "http://localhost:9081/locations";

    @Test
    public void getLocationsTest()
    {
        Location[] locations = sendGet(BASE_URL, Location[].class);

        Assert.assertNotNull(locations);
        Assert.assertEquals(3, locations.length);
    }
}