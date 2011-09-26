package com.googlecode.totallylazy.records;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callable2;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Sequence;

import java.util.HashMap;
import java.util.Map;

public interface Record {
    <T> T get(Keyword<T> keyword);

    <T> Record set(Keyword<T> name, T value);

    Sequence<Pair<Keyword, Object>> fields();

    Sequence<Keyword> keywords();

    Sequence<Object> getValuesFor(Sequence<Keyword> keywords);
}
