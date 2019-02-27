package org.exercise.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.MediaType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.exercise.StoreApp;
import org.junit.After;
import org.junit.Before;

import static javax.ws.rs.core.HttpHeaders.CONTENT_TYPE;
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

    private <T> RestAPIResponse<T> sendRequest(String url, String method, T value, Class<T> valueType)
    {
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            con.setRequestMethod(method);
            //add request header
            con.setRequestProperty(USER_AGENT, "Integration tests");
            if (value != null)
            {
                writeContent(con, value);
            }

            int responseCode = con.getResponseCode();
            logger.debug("Sending '{}' request to URL : {}", method, url);
            logger.debug("Response Code : {}", responseCode);

            T element = null;
            // checking for success
            if (isSuccess(responseCode) && valueType != null)
            {
                String response = readContent(con);
                element = jsonMapper.readValue(response, valueType);
            }
            return new RestAPIResponse<>(element, responseCode);
        }
        catch (IOException exc)
        {
            logger.error("Failed to send {} request to ur {}", method, url, exc);
        }
        return null;
    }

    private String readContent(HttpURLConnection con) throws IOException
    {
        StringBuilder response = new StringBuilder();
        try(BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream())))
        {
            String inputLine;

            while ((inputLine = in.readLine()) != null)
            {
                response.append(inputLine);
            }
        }
        return response.toString();
    }

    private <T> void writeContent(HttpURLConnection con, T value) throws IOException
    {
        con.setDoOutput(true);
        con.setRequestProperty(CONTENT_TYPE, MediaType.APPLICATION_JSON);
        try(BufferedWriter out = new BufferedWriter(new OutputStreamWriter(con.getOutputStream())))
        {
            String valueJson = jsonMapper.writeValueAsString(value);
            out.write(valueJson);
            out.flush();
        }
    }

    <T> RestAPIResponse<T> sendGet(String url, Class<T> valueType)
    {
        return sendRequest(url, HttpMethod.GET, null, valueType);
    }

    <T> RestAPIResponse<T> sendDelete(String url)
    {
        return sendRequest(url, HttpMethod.DELETE, null, null);
    }

    <T> RestAPIResponse<T> sendPost(String url, T value, Class<T> valueType)
    {
        return sendRequest(url, HttpMethod.POST, value, valueType);
    }

    private boolean isSuccess(int responseCode)
    {
        return responseCode / 100 == 2;
    }

    public class RestAPIResponse<T>
    {
        private T element;
        private int status;

        public RestAPIResponse(T element, int status)
        {
            this.element = element;
            this.status = status;
        }

        public T getElement() {
            return element;
        }

        public void setElement(T element) {
            this.element = element;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
