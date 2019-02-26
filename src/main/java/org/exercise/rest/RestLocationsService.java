package org.exercise.rest;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.exercise.service.ItemService;
import org.exercise.service.ItemServiceImpl;
import org.exercise.service.ServiceRegistry;

@Path("/locations")
@Produces(MediaType.APPLICATION_JSON)
@Singleton
public class RestLocationsService
{
    private ItemService itemService = (ItemService) ServiceRegistry.getService(ItemServiceImpl.class);

    @GET
    public Response getLocations()
    {
        // TODO add user authentication/authorization check if required.

        return Response.ok().entity(itemService.getAllLocations()).build();
    }
}
