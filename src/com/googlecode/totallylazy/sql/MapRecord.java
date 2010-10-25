package com.googlecode.totallylazy.sql;

import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Sequence;

import java.util.HashMap;
import java.util.Map;

import static com.googlecode.totallylazy.Callables.entryToPair;
import static com.googlecode.totallylazy.Sequences.sequence;

public class MapRecord implements Record{
    private final Map<Keyword, Object> fields = new HashMap<Keyword, Object>();

    public <T> T get(Keyword<T> keyword)  {
        return (T) fields.get(keyword);
    }

    public <T> Record set(Keyword<T> name, T value) {
        fields.put(name, value);
        return this;
    }

    public Sequence<Pair<Keyword, Object>> fields() {
        return sequence(fields.entrySet()).map(entryToPair(Keyword.class, Object.class));
    }

    @Override
    public String toString() {
        return fields().toString();
    }

    public static Record record() {
        return new MapRecord();
    }
}
