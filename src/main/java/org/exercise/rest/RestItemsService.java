package org.exercise.rest;

import org.exercise.service.ItemService;
import org.exercise.service.ItemServiceImpl;
import org.exercise.service.model.Item;

import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/items")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Singleton
public class RestItemsService
{
    private ItemService itemService = new ItemServiceImpl();

    @GET
    public Response getAllItems()
    {
        // TODO add user authentication/authorization check if required.

        return Response.ok(itemService.getAllItems()).build();
    }

    @GET
    @Path("{itemId}")
    public Response getItem(@QueryParam("itemId") int id)
    {
        // TODO add user authentication/authorization check if required.

        Item item = itemService.getItem(id);
        if (item != null)
        {
            return Response.ok(item).build();
        }
        Error error = new Error("not_found", "item not found");
        return Response.status(Response.Status.NOT_FOUND).entity(error).build();
    }

    @DELETE
    @Path("{itemId}")
    public Response removeItem(@QueryParam("itemId") int id)
    {
        // TODO add user authentication/authorization check if required.

        itemService.deleteItem(id);
        return Response.ok().build();
    }
}
