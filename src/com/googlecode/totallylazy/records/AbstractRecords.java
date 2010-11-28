package com.googlecode.totallylazy.records;

import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Sequence;

import static com.googlecode.totallylazy.Callables.first;
import static com.googlecode.totallylazy.Sequences.sequence;

public abstract class AbstractRecords implements Records{
    public Number add(Keyword recordName, Record... records) {
        return add(recordName, sequence(records));
    }

    public Number add(Keyword recordName, Sequence<Record> records) {
        return add(recordName, records.first().keywords(), records);
    }

    public Number set(Keyword recordName, Predicate<Record> predicate, Record record) {
        return set(recordName, predicate, record.keywords(), record);
    }
}
