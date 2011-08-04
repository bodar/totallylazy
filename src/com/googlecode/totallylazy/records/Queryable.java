package com.googlecode.totallylazy.records;

import com.googlecode.totallylazy.Sequence;

public interface Queryable<T> {
    Sequence<Record> query(final T query, final Sequence<Keyword> definitions);
}
