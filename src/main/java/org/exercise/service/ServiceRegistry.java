package org.exercise.service;

import java.util.HashMap;
import java.util.Map;

public class ServiceRegistry
{
    private static Map<Class, Object> services = new HashMap<>();

    public static Object getService(Class clazz)
    {
        return services.get(clazz);
    }

    public static void setService(Class clazz, Object service)
    {
        if (clazz != null && service != null)
        {
            services.put(clazz, service);
        }
    }
}
