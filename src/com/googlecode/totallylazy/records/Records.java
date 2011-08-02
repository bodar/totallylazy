package com.googlecode.totallylazy.records;

import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Sequence;

public interface Records {
    Sequence<Record> get(Keyword recordName);

    void define(Keyword recordName, Keyword<?>... fields);

    boolean exists(Keyword recordName);

    Number add(Keyword recordName, Record... records);

    Number add(Keyword recordName, Sequence<Record> records);

    Number set(Keyword recordName, Pair<? extends Predicate<? super Record>, Record>... records);

    Number set(Keyword recordName, Sequence<Pair<? extends Predicate<? super Record>, Record>> records);

    Number remove(Keyword recordName, Predicate<? super Record> predicate);

    Number remove(Keyword recordName);
}
