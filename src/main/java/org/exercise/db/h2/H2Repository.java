package org.exercise.db.h2;

import org.exercise.db.AbstractRepository;
import org.hibernate.cfg.Environment;

import java.net.URL;
import java.util.Properties;

public class H2Repository extends AbstractRepository
{
    protected Properties getDbConfiguration()
    {
        Properties settings = new Properties();
        settings.put(Environment.DRIVER, "org.h2.Driver");
        settings.put(Environment.URL, createDbUrl());
        settings.put(Environment.USER, "sa");
        settings.put(Environment.PASS, "");
        settings.put(Environment.DIALECT, "org.hibernate.dialect.H2Dialect");
//        settings.put(ENTITY_PACKAGE_KEY, "org.exercise.service.model");
        return settings;
    }

    private String createDbUrl()
    {
        String url = "jdbc:h2:mem:store";
        ClassLoader classLoader = getClass().getClassLoader();
        URL fileUrl = classLoader.getResource("sql/h2_schema.sql");
        if (fileUrl != null)
        {
            url += ";INIT=runscript from '" + fileUrl +"'";
        }
        return url;
    }
}
