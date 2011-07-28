package com.googlecode.totallylazy.records;

import com.googlecode.totallylazy.Sequence;

import java.util.Iterator;

public interface Queryable {
    Iterator<Record> query(final ParameterisedExpression expression, final Sequence<Keyword> definitions);
}
