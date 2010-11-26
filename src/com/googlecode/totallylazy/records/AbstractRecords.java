package com.googlecode.totallylazy.records;

import com.googlecode.totallylazy.Sequence;

import static com.googlecode.totallylazy.Callables.first;
import static com.googlecode.totallylazy.Sequences.sequence;

public abstract class AbstractRecords implements Records{
    public Number add(Keyword recordName, Record... records) {
        return add(recordName, sequence(records));
    }

    public Number add(Keyword recordName, Sequence<Record> records) {
        return add(recordName, records.first().fields().map(first(Keyword.class)).realise(), records);
    }
}
