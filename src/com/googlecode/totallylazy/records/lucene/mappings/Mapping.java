package com.googlecode.totallylazy.records.lucene.mappings;

import org.apache.lucene.document.Fieldable;
import org.apache.lucene.search.Query;

public interface Mapping<T> {
    Fieldable toField(String name, T value);

    T toValue(Fieldable fieldable);

    Query equalTo(String name, T value);

    Query notNull(String name);

    Query greaterThan(String name, T value);

    Query greaterThanOrEqual(String name, T value);

    Query lessThan(String name, T value);

    Query lessThanOrEqual(String name, T value);

    Query between(String name, T lower, T upper);
}
