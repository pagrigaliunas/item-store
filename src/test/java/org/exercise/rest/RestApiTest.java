package org.exercise.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.exercise.StoreApp;
import org.junit.After;
import org.junit.Before;

import static javax.ws.rs.core.HttpHeaders.USER_AGENT;

public class RestApiTest
{
    private static final String LOG4J_CONFIG_PROP_NAME = "log4j.configurationFile";

    private static final Logger logger;
    static
    {
        String property = System.getProperty(LOG4J_CONFIG_PROP_NAME);
        if (property == null || property.isEmpty())
        {
            System.setProperty(LOG4J_CONFIG_PROP_NAME, ".\\log4j2.properties");
        }
        logger = LogManager.getLogger(RestApiTest.class);
    }

    private StoreApp app;
    private ObjectMapper jsonMapper;

    @Before
    public void startServer()
    {
        app = new StoreApp();
        app.start();

        jsonMapper = new ObjectMapper();
    }

    @After
    public void stopServer()
    {
        app.stop();
    }

    private <T> T sendRequest(String url, String method, Class<T> valueType)
    {
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // optional default is GET
            con.setRequestMethod(method);

            //add request header
            con.setRequestProperty("User-Agent", USER_AGENT);

            int responseCode = con.getResponseCode();
            logger.debug("Sending '{}' request to URL : {}", method, url);
            logger.debug("Response Code : {}", responseCode);

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null)
            {
                response.append(inputLine);
            }
            in.close();
            return jsonMapper.readValue(response.toString(), valueType);
        }
        catch (IOException exc)
        {
            logger.error("Failed to send {} request to ur {}", method, url, exc);
        }
        return null;
    }

    public <T> T sendGet(String url, Class<T> valueType)
    {
        return sendRequest(url, "GET", valueType);
    }

    public <T> T sendPost(String url, Class<T> valueType)
    {
        return sendRequest(url, "POST", valueType);
    }
}
