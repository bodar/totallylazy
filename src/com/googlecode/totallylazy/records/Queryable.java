package com.googlecode.totallylazy.records;

import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.records.sql.Query;
import com.googlecode.totallylazy.records.sql.RecordIterator;

import java.util.Iterator;

public interface Queryable {
    Iterator<Record> query(Query query);
}
