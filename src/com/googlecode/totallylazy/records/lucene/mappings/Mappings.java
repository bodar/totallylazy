package com.googlecode.totallylazy.records.lucene.mappings;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.records.Keyword;
import org.apache.lucene.document.Fieldable;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.Sequences.iterate;
import static com.googlecode.totallylazy.numbers.Numbers.increment;

public class Mappings {
    private final Map<Class, Mapping<Object>> map = new HashMap<Class, Mapping<Object>>();

    public Mappings() {
        add(Date.class, new DateMapping());
        add(Integer.class, new IntegerMapping());
        add(Long.class, new LongMapping());
        add(String.class, new StringMapping());
        add(Object.class, new ObjectMapping());
    }

    public <T> void add(final Class<T> type, final Mapping<T> mapping) {
        map.put(type, (Mapping<Object>) mapping);
    }

//    public Object getValue(final ResultSet resultSet, Integer index, final Class aClass) throws SQLException {
//        if (map.containsKey(aClass)) {
//            return map.get(aClass).getValue(resultSet, index);
//        }
//        return map.get(Object.class).getValue(resultSet, index);
//    }
//
//    public void addValues(PreparedStatement statement, Sequence<Object> values) throws SQLException {
//        for (Pair<Integer, Object> pair : iterate(increment(), 1).safeCast(Integer.class).zip(values)) {
//            Integer index = pair.first();
//            Object value = pair.second();
//            if (value != null && map.containsKey(value.getClass())) {
//                map.get(value.getClass()).setValue(statement, index, value);
//            } else {
//                map.get(Object.class).setValue(statement, index, value);
//            }
//        }
//    }

    public Callable1<? super Fieldable, Pair<Keyword, Object>> asPair() {
        return new Callable1<Fieldable, Pair<Keyword, Object>>() {
            public Pair<Keyword, Object> call(Fieldable fieldable) throws Exception {
                Keyword keyword = Keyword.keyword(fieldable.name());
                return pair(keyword, (Object) fieldable.stringValue());
            }
        };
    }

}
