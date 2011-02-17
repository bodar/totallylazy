package com.googlecode.totallylazy.records;

import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.records.sql.Query;
import com.googlecode.totallylazy.records.sql.RecordIterator;

import java.util.Iterator;

public interface Queryable {
    Iterator<Record> query(Pair<String, Sequence<Object>> pair);
    Iterator<Record> query(String expression, Sequence<?> parameters);
}
