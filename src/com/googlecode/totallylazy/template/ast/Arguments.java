package com.googlecode.totallylazy.template.ast;

import com.googlecode.totallylazy.Pair;

public interface Arguments<T> extends Expression, Iterable<Pair<T,Expression>> {
}
