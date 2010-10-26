package com.googlecode.totallylazy.sql;

import com.googlecode.totallylazy.Callable2;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Sequence;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import static com.googlecode.totallylazy.Callables.asHashCode;
import static com.googlecode.totallylazy.Callables.entryToPair;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.matchers.IterableMatcher.hasExactly;

public class MapRecord implements Record{
    private final Map<Keyword, Object> fields = new LinkedHashMap<Keyword, Object>();

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

    @Override
    public boolean equals(Object other) {
        if (other instanceof MapRecord) {
            MapRecord otherRecord = (MapRecord) other;
            if(fields.size() != otherRecord.fields.size()){
                return false;
            }
            for (Pair<Keyword, Object> entry : fields()) {
                if(!otherRecord.fields().contains(entry)){
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