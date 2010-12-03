package com.googlecode.totallylazy.records;

import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Sequence;

public interface Records {
    Sequence<Record> get(Keyword recordName);

    void define(Keyword recordName, Keyword<?>... fields);

    Number add(Keyword recordName, Record... records);

    Number add(Keyword recordName, Sequence<Record> records);

    Number add(Keyword recordName, Sequence<Keyword> fields, Sequence<Record> records);

    Number set(Keyword recordName, Predicate<? super Record> predicate, Record record);

    Number set(Keyword recordName, Predicate<? super Record> predicate, Sequence<Keyword> fields, Record record);

    Number remove(Keyword recordName, Predicate<? super Record> predicate);
}
