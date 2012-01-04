package com.googlecode.totallylazy.records.sql.mappings;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Function1;
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
import java.util.UUID;

import static com.googlecode.totallylazy.Sequences.iterate;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.numbers.Numbers.increment;
import static com.googlecode.totallylazy.numbers.Numbers.numbers;
import static com.googlecode.totallylazy.numbers.Numbers.range;
import static com.googlecode.totallylazy.numbers.Numbers.sum;

public class Mappings {
    private final Map<Class, Mapping<Object>> map = new HashMap<Class, Mapping<Object>>();

    public Mappings() {
        add(Date.class, new DateMapping());
        add(Timestamp.class, new TimestampMapping());
        add(Integer.class, new IntegerMapping());
        add(Long.class, new LongMapping());
        add(String.class, new StringMapping());
        add(URI.class, new UriMapping());
        add(Boolean.class, new BooleanMapping());
        add(UUID.class, new UUIDMapping());
        add(Object.class, new ObjectMapping());
    }

    @SuppressWarnings("unchecked")
    public <T> Mappings add(final Class<T> type, final Mapping<T> mapping) {
        map.put(type, (Mapping<Object>) mapping);
        return this;
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
        for (Pair<Integer, Object> pair : range(1).safeCast(Integer.class).zip(values)) {
            Integer index = pair.first();
            Object value = pair.second();
            get(value == null ? Object.class : value.getClass()).setValue(statement, index, value);
        }
    }

    public Function1<PreparedStatement, Number> addValuesInBatch(final Sequence<? extends Iterable<Object>> allValues) {
        return new Function1<PreparedStatement, Number>() {
            public Number call(PreparedStatement statement) throws Exception {
                for (Iterable<Object> values : allValues) {
                    addValues(statement, sequence(values));
                    statement.addBatch();
                }
                return numbers(statement.executeBatch()).reduce(sum());
            }
        };
    }


}
