package com.googlecode.totallylazy.records;

import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Sequence;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.googlecode.totallylazy.Callables.asHashCode;
import static com.googlecode.totallylazy.Callables.entryToPair;
import static com.googlecode.totallylazy.Callables.first;
import static com.googlecode.totallylazy.Callables.second;
import static com.googlecode.totallylazy.Predicates.in;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Predicates.where;
import static com.googlecode.totallylazy.Sequences.sequence;

public class MapRecord implements Record {
    private final Map<Keyword, Object> fields = new LinkedHashMap<Keyword, Object>();

    public <T> T get(Keyword<T> keyword) {
        return (T) fields.get(keyword);
    }

    public <T> Record set(Keyword<T> name, T value) {
        fields.put(name, value);
        return this;
    }

    public Sequence<Pair<Keyword, Object>> fields() {
        return sequence(fields.entrySet()).map(entryToPair(Keyword.class, Object.class));
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
    public boolean equals(Object other) {
        if (other instanceof MapRecord) {
            MapRecord otherRecord = (MapRecord) other;
            if (fields.size() != otherRecord.fields.size()) {
                return false;
            }
            for (Pair<Keyword, Object> entry : fields()) {
                if (!otherRecord.fields().contains(entry)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return fields().foldLeft(31, asHashCode());
    }
}
