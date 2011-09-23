package com.googlecode.totallylazy.records.xml.mappings;

import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Mappings {
    private final Map<Class, Mapping<Object>> map = new HashMap<Class, Mapping<Object>>();

    public Mappings() {
        add(Date.class, DateMapping.defaultFormat());
        add(Integer.class, new IntegerMapping());
        add(Long.class, new LongMapping());
        add(String.class, new StringMapping());
        add(URI.class, new UriMapping());
        add(Boolean.class, new BooleanMapping());
        add(Object.class, new ObjectMapping());
    }

    public <T> Mappings add(final Class<T> type, final Mapping<T> mapping) {
        map.put(type, (Mapping<Object>) mapping);
        return this;
    }

    public Mapping<Object> get(final Class aClass) {
        if(!map.containsKey(aClass)) {
            return map.get(Object.class);
        }
        return map.get(aClass);
    }
}
