package org.exercise.rest;

import org.exercise.service.ItemService;
import org.exercise.service.ItemServiceImpl;
import org.exercise.service.ServiceRegistry;
import org.exercise.service.model.Item;

import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.exercise.service.validation.ValidationException;

@Path("/items")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Singleton
public class RestItemsService
{
    private ItemService itemService = (ItemService) ServiceRegistry.getService(ItemServiceImpl.class);

    @GET
    public Response getAllItems()
    {
        // TODO add user authentication/authorization check if required.

        return Response.ok(itemService.getAllItems()).build();
    }

    @GET
    @Path("{itemId}")
    public Response getItem(@PathParam("itemId") int id)
    {
        // TODO add user authentication/authorization check if required.

        Item item = itemService.getItem(id);
        if (item != null)
        {
            return Response.ok(item).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }


    @POST
    public Response addItem(Item item)
    {
        // TODO add user authentication/authorization check if required.

        try
        {
            itemService.addItem(item);
        }
        catch (ValidationException exc)
        {
            return Response.status(Response.Status.BAD_REQUEST).entity(exc.getMessage()).build();
        }
        return Response.ok().entity(item).build();
    }

    @PATCH
    @Path("{itemId}")
    public Response updateItem(Item item)
    {
        // TODO add user authentication/authorization check if required.

        try
        {
            itemService.updateItem(item);
        }
        catch (ValidationException exc)
        {
            return Response.status(Response.Status.BAD_REQUEST).entity(exc.getMessage()).build();
        }
        return Response.ok().build();
    }

    @DELETE
    @Path("{itemId}")
    public Response removeItem(@PathParam("itemId") int id)
    {
        // TODO add user authentication/authorization check if required.

        itemService.deleteItem(id);
        return Response.ok().build();
    }
}
