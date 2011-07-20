package com.googlecode.totallylazy.records.xml.mappings;

import com.googlecode.totallylazy.*;
import com.googlecode.totallylazy.records.Keyword;
import com.googlecode.totallylazy.records.MapRecord;
import com.googlecode.totallylazy.records.Record;
import org.w3c.dom.Node;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.Predicates.*;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Strings.equalIgnoringCase;
import static com.googlecode.totallylazy.records.Keyword.keyword;
import static com.googlecode.totallylazy.records.Keywords.name;
import static com.googlecode.totallylazy.records.memory.MemoryRecords.updateValues;

public class Mappings {
    private final Map<Class, Mapping<Object>> map = new HashMap<Class, Mapping<Object>>();

    public Mappings() {
        add(Date.class, new DateMapping());
        add(Integer.class, new IntegerMapping());
        add(Long.class, new LongMapping());
        add(Object.class, new ObjectMapping());
    }

    public <T> void add(final Class<T> type, final Mapping<T> mapping) {
        map.put(type, (Mapping<Object>) mapping);
    }

    public Mapping<Object> get(final Class aClass) {
        if(!map.containsKey(aClass)) {
            return map.get(Object.class);
        }
        return map.get(aClass);
    }

    private Keyword getKeyword(String name, Sequence<Keyword> definitions) {
        return definitions.find(where(name(), equalIgnoringCase(name))).getOrElse(keyword(name));
    }

}
