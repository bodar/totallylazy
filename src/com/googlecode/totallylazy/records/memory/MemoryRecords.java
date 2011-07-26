package com.googlecode.totallylazy.records.memory;

import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.numbers.Numbers;
import com.googlecode.totallylazy.records.AbstractRecords;
import com.googlecode.totallylazy.records.Keyword;
import com.googlecode.totallylazy.records.Record;
import com.googlecode.totallylazy.records.RecordCallables;

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
import static com.googlecode.totallylazy.records.RecordCallables.updateValues;
import static com.googlecode.totallylazy.records.SelectCallable.select;

public class MemoryRecords extends AbstractRecords {
    private final Map<Keyword, List<Record>> memory = new HashMap<Keyword, List<Record>>();

    public Sequence<Record> get(Keyword recordName) {
        return sequence(recordsFor(recordName));
    }

    private List<Record> recordsFor(Keyword recordName) {
        if (!exists(recordName)) {
            memory.put(recordName, new ArrayList<Record >());
        }
        return memory.get(recordName);
    }

    public boolean exists(Keyword recordName) {
        return memory.containsKey(recordName);
    }

    public Number add(Keyword recordName, Sequence<Keyword> fields, Sequence<Record> records) {
        if (records.isEmpty()) {
            return 0;
        }

        List<Record> list = recordsFor(recordName);
        Number count = 0;
        for (Record record : records.map(select(fields))) {
            list.add(record);
            count = increment(count);
        }
        return count;
    }

    public Number remove(Keyword recordName, Predicate<? super Record> predicate) {
        List<Record> matches = sequence(recordsFor(recordName)).
                filter(predicate).
                toList();

        recordsFor(recordName).removeAll(matches);

        return matches.size();
    }

    public Number remove(Keyword recordName) {
        List<Record> records = recordsFor(recordName);
        int count = records.size();
        memory.remove(recordName);
        return count;
    }

    @SuppressWarnings({"unchecked"})
    public Number set(Keyword recordName, Predicate<? super Record> predicate, Sequence<Keyword> fields, Record record) {
        List<Record> records = recordsFor(recordName);
        Number count = 0;
        Sequence<Integer> indexes = get(recordName).zipWithIndex().
                filter(where(second(Record.class), is(predicate))).
                map(first(Number.class)).safeCast(Integer.class);
        for (Integer index : indexes) {
            records.set(index, record.fields().fold(records.get(index), updateValues()));
        }
        count = Numbers.add(count, indexes.size());
        return count;
    }

}
