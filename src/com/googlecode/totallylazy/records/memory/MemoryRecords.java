package com.googlecode.totallylazy.records.memory;

import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.numbers.Numbers;
import com.googlecode.totallylazy.records.AbstractRecords;
import com.googlecode.totallylazy.records.Keyword;
import com.googlecode.totallylazy.records.Record;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.numbers.Numbers.increment;
import static com.googlecode.totallylazy.records.SelectCallable.select;

public class MemoryRecords extends AbstractRecords {
    private final Map<Keyword, List<Record>> memory = new HashMap<Keyword, List<Record>>();

    public Sequence<Record> query(Keyword recordName) {
        return sequence(memory.get(recordName));
    }

    public void define(Keyword recordName, Keyword<?>... fields) {
    }

    public Number add(Keyword recordName, Sequence<Keyword> fields, Sequence<Record> records) {
        if(!memory.containsKey(recordName)){
            memory.put(recordName, new ArrayList<Record>());
        }
        List<Record> list = memory.get(recordName);
        Number count = 0;
        for (Record record : records.map(select(fields)).realise()) {
            list.add(record);
            count = increment(count);
        }
        return count;
    }
}
