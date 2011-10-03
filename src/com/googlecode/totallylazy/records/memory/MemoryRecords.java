package com.googlecode.totallylazy.records.memory;

import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.records.AbstractRecords;
import com.googlecode.totallylazy.records.Keyword;
import com.googlecode.totallylazy.records.Record;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.numbers.Numbers.increment;

public class MemoryRecords extends AbstractRecords {
    private final Map<Keyword, List<Record>> memory = new HashMap<Keyword, List<Record>>();

    public Sequence<Record> get(Keyword recordName) {
        return sequence(recordsFor(recordName));
    }

    private List<Record> recordsFor(Keyword recordName) {
        if (!memory.containsKey(recordName)) {
            memory.put(recordName, new ArrayList<Record >());
        }
        return memory.get(recordName);
    }

    public Number add(Keyword recordName, Sequence<Record> records) {
        if (records.isEmpty()) {
            return 0;
        }

        List<Record> list = recordsFor(recordName);
        Number count = 0;
        for (Record record : records) {
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
}
