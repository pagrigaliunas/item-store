package org.exercise.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
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
    private static final Logger logger;
    static
    {
        String property = System.getProperty(StoreApp.LOG4J_CONFIG_PROP_NAME);
        if (property == null || property.isEmpty())
        {
            System.setProperty(StoreApp.LOG4J_CONFIG_PROP_NAME, ".\\log4j2.properties");
        }
        logger = LogManager.getLogger(RestApiTest.class);

        allowUnsupportedHttpMethods("PATCH");
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

    ObjectMapper getJsonMapper()
    {
        return jsonMapper;
    }

    private <T, V> RestAPIResponse<T> sendRequest(String url, String method, V value, Class<T> resultType)
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
            if (isSuccess(responseCode) && resultType != null)
            {
                String response = readContent(con);
                element = jsonMapper.readValue(response, resultType);
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

    private <V> void writeContent(HttpURLConnection con, V value) throws IOException
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

    <T> RestAPIResponse<T> sendGet(String url, Class<T> resultType)
    {
        return sendRequest(url, HttpMethod.GET, null, resultType);
    }

    <T> RestAPIResponse<T> sendDelete(String url)
    {
        return sendRequest(url, HttpMethod.DELETE, null, null);
    }

    <T, V> RestAPIResponse<T> sendPost(String url, V value, Class<T> resultType)
    {
        return sendRequest(url, HttpMethod.POST, value, resultType);
    }

    <T, V> RestAPIResponse<T> sendPatch(String url, V value, Class<T> resultType)
    {
        return sendRequest(url, HttpMethod.PATCH, value, resultType);
    }

    private boolean isSuccess(int responseCode)
    {
        return responseCode / 100 == 2;
    }

    private static void allowUnsupportedHttpMethods(String... methods)
    {
        try
        {
            Field methodsField = HttpURLConnection.class.getDeclaredField("methods");

            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(methodsField, methodsField.getModifiers() & ~Modifier.FINAL);

            methodsField.setAccessible(true);

            String[] oldMethods = (String[]) methodsField.get(null);
            Set<String> methodsSet = new LinkedHashSet<>(Arrays.asList(oldMethods));
            methodsSet.addAll(Arrays.asList(methods));
            String[] newMethods = methodsSet.toArray(new String[0]);

            methodsField.set(null/*static field*/, newMethods);
        }
        catch (NoSuchFieldException | IllegalAccessException e)
        {
            throw new IllegalStateException(e);
        }
    }

    class RestAPIResponse<T>
    {
        private T element;
        private int status;

        RestAPIResponse(T element, int status)
        {
            this.element = element;
            this.status = status;
        }

        T getElement() {
            return element;
        }

        int getStatus() {
            return status;
        }
    }
}
