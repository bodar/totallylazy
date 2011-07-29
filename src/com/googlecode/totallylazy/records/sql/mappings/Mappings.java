package com.googlecode.totallylazy.records.sql.mappings;

import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Sequence;

import java.net.URI;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.googlecode.totallylazy.Sequences.iterate;
import static com.googlecode.totallylazy.numbers.Numbers.increment;

public class Mappings {
    private final Map<Class, Mapping<Object>> map = new HashMap<Class, Mapping<Object>>();

    public Mappings() {
        add(Date.class, new DateMapping());
        add(Timestamp.class, new TimestampMapping());
        add(Integer.class, new IntegerMapping());
        add(Long.class, new LongMapping());
        add(String.class, new StringMapping());
        add(URI.class, new UriMapping());
        add(Object.class, new ObjectMapping());
    }

    public <T> void add(final Class<T> type, final Mapping<T> mapping) {
        map.put(type, (Mapping<Object>) mapping);
    }

    public Mapping<Object> get(final Class aClass) {
        if (!map.containsKey(aClass)) {
            return map.get(Object.class);
        }
        return map.get(aClass);
    }

    public Object getValue(final ResultSet resultSet, Integer index, final Class aClass) throws SQLException {
        return get(aClass).getValue(resultSet, index);
    }

    public void addValues(PreparedStatement statement, Sequence<Object> values) throws SQLException {
        for (Pair<Integer, Object> pair : iterate(increment(), 1).safeCast(Integer.class).zip(values)) {
            Integer index = pair.first();
            Object value = pair.second();
            get(value == null ? Object.class : value.getClass()).setValue(statement, index, value);
        }
    }

    public String getType(Class<?> aClass) {
        return get(aClass).type();
    }
}