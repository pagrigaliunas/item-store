package org.exercise;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Config
{
    private static final String LOG4J_CONFIG_PROP_NAME = "log4j.configurationFile";
    private static final String STORE_APP_CONFIG_PROP_NAME = "storeApp.configurationFile";

    private static final String DB_NAME_KEY = "db.name";
    private static final String JDBC_URL_KEY = "jdbc.url";
    private static final String HTTP_PORT_KEY = "http.port";

    private static final Logger logger;
    private static Config config;

    private Properties properties = new Properties();

    static
    {
        String property = System.getProperty(LOG4J_CONFIG_PROP_NAME);
        if (property == null || property.isEmpty())
        {
            System.setProperty(LOG4J_CONFIG_PROP_NAME, ".\\conf\\log4j2.properties");
        }

        property = System.getProperty(STORE_APP_CONFIG_PROP_NAME);
        if (property == null || property.isEmpty())
        {
            System.setProperty(STORE_APP_CONFIG_PROP_NAME, ".\\conf\\storeApp.properties");
        }
        logger = LogManager.getLogger(Config.class);
    }

    private Config()
    {
    }

    public static Config getIntance()
    {
        if (config == null)
        {
            config = new Config();
            config.load();
        }
        return config;
    }

    private void load()
    {
        String filePath = System.getProperty(STORE_APP_CONFIG_PROP_NAME);
        if (filePath != null && !filePath.isEmpty())
        {
            try
            {
                properties.load(new FileReader(filePath));
            }
            catch (IOException exc)
            {
                logger.error("Failed to load properties file. Using defaults.", exc);
                setDefaults();
            }
        }
        else
        {
            setDefaults();
        }
    }

    private void setDefaults() {
        // setting defaults
        properties.setProperty(DB_NAME_KEY, "store");
        properties.setProperty(JDBC_URL_KEY, "jdbc:h2:mem");
        properties.setProperty(HTTP_PORT_KEY, "8080");
    }

    public int getPort()
    {
        return Integer.valueOf(properties.getProperty(HTTP_PORT_KEY));
    }

    public String getJdbcUrl()
    {
        return properties.getProperty(JDBC_URL_KEY);
    }

    public String getDbName()
    {
        return properties.getProperty(DB_NAME_KEY);
    }
}
