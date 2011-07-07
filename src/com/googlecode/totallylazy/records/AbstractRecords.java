package com.googlecode.totallylazy.records;

import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Sequence;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.googlecode.totallylazy.Arrays.list;
import static com.googlecode.totallylazy.Sequences.sequence;

public abstract class AbstractRecords implements Records{
    private final Map<Keyword, List<Keyword<?>>> definitions = new HashMap<Keyword, List<Keyword<?>>>();

    public void define(Keyword recordName, Keyword<?>... fields) {
        definitions.put(recordName, list(fields));
    }

    public List<Keyword> definitions(Keyword recordName) {
        if(!definitions.containsKey(recordName)){
            return list();
        }
        return (List) definitions.get(recordName);
    }

    public Number add(Keyword recordName, Record... records) {
        return add(recordName, sequence(records));
    }

    public Number add(Keyword recordName, Sequence<Record> records) {
        if(records.isEmpty()) return 0;
        return add(recordName, records.first().keywords(), records);
    }

    public Number set(Keyword recordName, Predicate<? super Record> predicate, Record record) {
        return set(recordName, predicate, record.keywords(), record);
    }
}
