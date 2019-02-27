package org.exercise;

import com.sun.net.httpserver.HttpServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.exercise.db.h2.H2Repository;
import org.exercise.rest.RestItemsService;
import org.exercise.rest.RestLocationsService;
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

    private HttpServer httpServer;
    private H2Repository repository;

    public void start()
    {
        repository = new H2Repository();
        repository.open();

        ServiceRegistry.setService(ItemServiceImpl.class, new ItemServiceImpl(repository));

        logger.info("Starting HTTP server...");
        int port = 9081;
        URI baseUri = UriBuilder.fromUri("http://localhost/").port(9081).build();
        ResourceConfig config = new ResourceConfig(
                JacksonFeature.class,
                RestItemsService.class,
                RestLocationsService.class);
        // starting server
        httpServer = JdkHttpServerFactory.createHttpServer(baseUri, config);
        logger.info("HTTP server started on port " + port + ".");
    }

    public void stop()
    {
        if (httpServer != null)
        {
            httpServer.stop(0);
        }

        if (repository != null)
        {
            repository.close();
        }
    }

    public static void main(String[] args)
    {
        logger.info("Starting Store Application...");
        new StoreApp().start();
        logger.info("Store Application started.");
    }
}
