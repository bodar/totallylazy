package com.googlecode.totallylazy.sql;

import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Sequence;

import java.util.HashMap;
import java.util.Map;

import static com.googlecode.totallylazy.Callables.entryToPair;
import static com.googlecode.totallylazy.Sequences.sequence;

public class MapRecord implements Record{
    private final Map<Keyword, Object> fields = new HashMap<Keyword, Object>();
    private final Keyword name;

    protected MapRecord(Keyword name) {
        this.name = name;
    }

    public <T> T get(Keyword<T> keyword)  {
        return (T) fields.get(keyword);
    }

    public <T> Record set(Keyword<T> name, T value) {
        fields.put(name, value);
        return this;
    }

    public Keyword name() {
        return name;
    }

    public Sequence<Pair<Keyword, Object>> fields() {
        return sequence(fields.entrySet()).map(entryToPair(Keyword.class, Object.class));
    }

    @Override
    public String toString() {
        return String.format("%s(%s)", name, fields());
    }

    public static Record record(Keyword name) {
        return new MapRecord(name);
    }
}
