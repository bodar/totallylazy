package com.googlecode.totallylazy.records;

import java.util.Iterator;

public interface Queryable {
    Iterator<Record> query(ParameterisedExpression expression);
}
