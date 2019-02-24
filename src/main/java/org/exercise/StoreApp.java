package org.exercise;

import org.exercise.rest.RestItemsService;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;

public class StoreApp
{
    private void run()
    {


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
