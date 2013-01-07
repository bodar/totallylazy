package com.googlecode.totallylazy;

import java.util.Properties;

public class PrefixProperties extends Properties {
    private final String prefix;

    public PrefixProperties(String prefix) {
        this.prefix = prefix;
    }

    public PrefixProperties(String prefix, Properties parent) {
        super(parent);
        this.prefix = prefix;
    }

    @Override
    public String getProperty(String key) {
        return super.getProperty(format(key));
    }

    @Override
    public synchronized Object setProperty(String key, String value) {
        return super.setProperty(format(key), value);
    }

    private String format(String key) {
        return String.format("%s.%s", prefix, key);
    }
}
