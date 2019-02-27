package org.exercise.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import java.io.IOException;

@Path("/items")
@Produces(MediaType.APPLICATION_JSON)
@Singleton
public class RestItemsService
{
    private static final Logger logger = LogManager.getLogger(RestItemsService.class);

    private ItemService itemService = (ItemService) ServiceRegistry.getService(ItemServiceImpl.class);
    private ObjectMapper jsonMapper = new ObjectMapper();

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getAllItems()
    {
        // TODO add user authentication/authorization check if required.

        return Response.ok(itemService.getAllItems()).build();
    }

    @GET
    @Path("{itemId}")
    @Consumes(MediaType.APPLICATION_JSON)
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
    @Consumes(MediaType.APPLICATION_JSON)
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
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON_PATCH_JSON})
    public Response updateItem(@PathParam("itemId") int id, JsonPatch patch)
    {
        // TODO add user authentication/authorization check if required.

        Item item = itemService.getItem(id);
        if (item == null)
        {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        try
        {
            item = applyChange(patch, item);
        }
        catch (IOException | JsonPatchException exc)
        {
            logger.error("Failed to apply Item patch.", exc);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        try
        {
            itemService.updateItem(item);
        }
        catch (ValidationException exc)
        {
            return Response.status(Response.Status.BAD_REQUEST).entity(exc.getMessage()).build();
        }
        return Response.ok().entity(item).build();
    }

    @DELETE
    @Path("{itemId}")
    public Response removeItem(@PathParam("itemId") int id)
    {
        // TODO add user authentication/authorization check if required.

        itemService.deleteItem(id);
        return Response.ok().build();
    }

    private Item applyChange(JsonPatch patch, Item item) throws IOException, JsonPatchException
    {
        JsonNode itemNode = jsonMapper.valueToTree(item);
        itemNode = patch.apply(itemNode);
        return jsonMapper.treeToValue(itemNode, Item.class);
    }
}
