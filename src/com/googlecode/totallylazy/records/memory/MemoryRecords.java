package com.googlecode.totallylazy.records.memory;

import com.googlecode.totallylazy.Callable2;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.numbers.Numbers;
import com.googlecode.totallylazy.records.AbstractRecords;
import com.googlecode.totallylazy.records.Keyword;
import com.googlecode.totallylazy.records.Record;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.googlecode.totallylazy.Callables.first;
import static com.googlecode.totallylazy.Callables.second;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Predicates.where;
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
        for (Record record : records.map(select(fields))) {
            list.add(record);
            count = increment(count);
        }
        return count;
    }

    public Number set(Keyword recordName, Pair<? extends Predicate<Record>, Record>... pairs) {
        List<Record> records = memory.get(recordName);
        Number count = 0;
        for (Pair<? extends Predicate<Record>, Record> pair : pairs) {
            Sequence<Integer> indexes = query(recordName).zipWithIndex().
                    filter(where(second(Record.class), is(pair.first()))).
                    map(first(Number.class)).safeCast(Integer.class);
            for (Integer index : indexes) {
                records.set(index, pair.second().fields().fold(records.get(index), updateValues()));
            }
            count = Numbers.add(count, indexes.size());
        }
        return count;
    }

    private Callable2<? super Record, ? super Pair<Keyword, Object>, Record> updateValues() {
        return new Callable2<Record, Pair<Keyword, Object>, Record>() {
            public Record call(Record record, Pair<Keyword, Object> field) throws Exception {
                return record.set(field.first(), field.second());
            }
        };
    }
}
