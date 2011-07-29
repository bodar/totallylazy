package com.googlecode.totallylazy.records;

import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.records.sql.Expression;

import java.util.Iterator;

public interface Queryable<T> {
    Sequence<Record> query(final T query, final Sequence<Keyword> definitions);
}
