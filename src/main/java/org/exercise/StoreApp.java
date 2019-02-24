package org.exercise;

import org.exercise.db.h2.H2Repository;
import org.exercise.rest.RestItemsService;
import org.exercise.service.ItemServiceImpl;
import org.exercise.service.ServiceRegistry;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;

public class StoreApp
{
    private void run()
    {

        H2Repository repository = new H2Repository();
        repository.init();

        ServiceRegistry.setService(ItemServiceImpl.class, new ItemServiceImpl(repository));

        URI baseUri = UriBuilder.fromUri("http://localhost/").port(80).build();
        ResourceConfig config = new ResourceConfig(RestItemsService.class, JacksonFeature.class);
        // starting server
        JdkHttpServerFactory.createHttpServer(baseUri, config);
    }

    public static void main(String args[])
    {
        new StoreApp().run();
    }
}
