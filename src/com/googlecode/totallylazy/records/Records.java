package com.googlecode.totallylazy.records;

import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.predicates.WherePredicate;
import com.googlecode.totallylazy.records.Keyword;
import com.googlecode.totallylazy.records.Record;
import com.googlecode.totallylazy.records.sql.RecordSequence;

public interface Records {
    Sequence<Record> query(Keyword recordName);

    void define(Keyword recordName, Keyword<?>... fields);

    Number add(Keyword recordName, Record... records);

    Number add(Keyword recordName, Sequence<Record> records);

    Number add(Keyword recordName, Sequence<Keyword> fields, Sequence<Record> records);

    Number set(Keyword recordName, Predicate<Record> predicate, Record record);

    Number set(Keyword recordName, Predicate<Record> predicate, Sequence<Keyword> fields, Record record);
}
