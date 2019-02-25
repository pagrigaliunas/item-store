package org.exercise;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    private static final Logger logger = LogManager.getLogger(StoreApp.class);

    private void run()
    {
        H2Repository repository = new H2Repository();
        repository.init();

        ServiceRegistry.setService(ItemServiceImpl.class, new ItemServiceImpl(repository));

        logger.info("Starting HTTP server...");
        URI baseUri = UriBuilder.fromUri("http://localhost/").port(9081).build();
        ResourceConfig config = new ResourceConfig(RestItemsService.class, JacksonFeature.class);
        // starting server
        JdkHttpServerFactory.createHttpServer(baseUri, config);
        logger.info("HTTP server started.");
    }

    public static void main(String[] args)
    {
        logger.info("Starting Store Application...");
        new StoreApp().run();
        logger.info("Store Application started.");
    }
}
