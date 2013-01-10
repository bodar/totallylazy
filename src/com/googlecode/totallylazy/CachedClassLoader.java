package com.googlecode.totallylazy;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CachedClassLoader extends ClassLoader {
    private final Map<String, Class> map = new ConcurrentHashMap<String, Class>();

    public CachedClassLoader(ClassLoader parent) {
        super(parent);
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        if (!map.containsKey(name)) {
            map.put(name, super.loadClass(name));
        }
        return  map.get(name);
    }
}