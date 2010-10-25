package com.googlecode.totallylazy.sql;

import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Sequence;

public interface Record {
    <T> T get(Keyword<T> keyword);

    <T> Record set(Keyword<T> name, T value);

    Sequence<Pair<Keyword, Object>> fields();
}
