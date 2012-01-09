package com.googlecode.totallylazy.records;

import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Sequence;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.googlecode.totallylazy.Callables.first;
import static com.googlecode.totallylazy.Callables.second;
import static com.googlecode.totallylazy.Maps.pairs;
import static com.googlecode.totallylazy.Predicates.in;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Predicates.where;
import static com.googlecode.totallylazy.Sequences.sequence;

public class MapRecord implements Record {
    private final Map<Keyword, Object> fields = new LinkedHashMap<Keyword, Object>();

    public <T> T get(Keyword<T> keyword) {
        return keyword.forClass().cast(fields.get(keyword));
    }

    public <T> Record set(Keyword<T> name, T value) {
        fields.put(name, value);
        return this;
    }

    public Sequence<Pair<Keyword, Object>> fields() {
        return pairs(fields);
    }

    public Sequence<Keyword> keywords() {
        return sequence(fields.keySet());
    }

    public Sequence<Object> getValuesFor(Sequence<Keyword> keywords) {
        return fields().filter(where(first(Keyword.class), is(in(keywords)))).map(second());
    }

    @Override
    public String toString() {
        return fields().toString();
    }

    public static Record record() {
        return new MapRecord();
    }

    @Override
    public final boolean equals(final Object o) {
        return o instanceof MapRecord && fields().equals(((MapRecord) o).fields());
    }

    @Override
    public final int hashCode() {
        return fields().hashCode();
    }
}
