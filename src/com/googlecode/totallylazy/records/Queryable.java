package com.googlecode.totallylazy.records;

import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.records.sql.Expression;

import java.util.Iterator;

public interface Queryable {
    Iterator<Record> query(final Expression expression, final Sequence<Keyword> definitions);
}
