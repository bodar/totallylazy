package com.googlecode.totallylazy.records.sql.expressions;

import com.googlecode.totallylazy.Sequence;

public interface Expression {
    String text();

    Sequence<Object> parameters();
}
