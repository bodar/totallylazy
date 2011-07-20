package com.googlecode.totallylazy.records;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.googlecode.totallylazy.Arrays.empty;
import static com.googlecode.totallylazy.Arrays.list;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.records.memory.MemoryRecords.updateValues;

public abstract class AbstractRecords implements Records{
    private final Map<Keyword, List<Keyword<?>>> definitions = new HashMap<Keyword, List<Keyword<?>>>();

    public void define(Keyword recordName, Keyword<?>... fields) {
        definitions.put(recordName, list(fields));
    }

    public Sequence<Keyword> definitions(Keyword recordName) {
        if(!definitions.containsKey(recordName)){
            return Sequences.empty();
        }
        return sequence((List) definitions.get(recordName));
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

    public Number set(Keyword recordName, Predicate<? super Record> predicate, Sequence<Keyword> fields, Record record) {
        Sequence<Record> updated = get(recordName).filter(predicate).map(updateWithFieldsIn(record)).realise();
        Number count = remove(recordName, predicate);
        add(recordName, updated);
        return count;
    }

    public Callable1<? super Record, Record> updateWithFieldsIn(final Record record) {
        return new Callable1<Record, Record>() {
            public Record call(Record recordToUpdate) throws Exception {
                return record.fields().fold(recordToUpdate, updateValues());
            }
        };
    }
}
