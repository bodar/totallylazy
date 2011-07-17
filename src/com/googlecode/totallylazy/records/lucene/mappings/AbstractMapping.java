package com.googlecode.totallylazy.records.lucene.mappings;

import com.googlecode.totallylazy.LazyException;
import org.apache.lucene.search.Query;

public abstract class AbstractMapping<T> implements Mapping<T> {
    public Query equalTo(String name, T value) {
        try {
            return newRange(name, value, value, true, true);
        } catch (Exception e) {
            throw new LazyException(e);
        }
    }

    public Query greaterThan(String name, T value) {
        try {
            return newRange(name, value, null, false, true);
        } catch (Exception e) {
            throw new LazyException(e);
        }
    }

    protected abstract Query newRange(String name, T lower, T upper, boolean minInclusive, boolean maxInclusive) throws Exception;
}
