package com.googlecode.totallylazy.records;

import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.records.Keyword;
import com.googlecode.totallylazy.records.Record;
import com.googlecode.totallylazy.records.sql.RecordSequence;

public interface Records {
    Sequence<Record> query(Keyword recordName);

    void define(Keyword recordName, Keyword<?>... fields);

    Number add(Keyword recordName, Record... records);

    Number add(Keyword recordName, Sequence<Record> records);

    Number add(Keyword recordName, Sequence<Keyword> fields, Sequence<Record> records);
}
