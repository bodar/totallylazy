package com.googlecode.totallylazy.records.lucene.mappings;

import com.googlecode.totallylazy.LazyException;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Fieldable;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermRangeQuery;

public abstract class AbstractStringMapping<T> extends AbstractMapping<T> {
    private Field.Index index;

    public AbstractStringMapping(final Field.Index index) {
        this.index = index;
    }

    protected abstract String toString(T value) throws Exception;

    protected abstract T fromString(final String value) throws Exception;

    public Fieldable toField(String name, T value) {
        try {
            return new Field(name, toString(value), Field.Store.YES, index);
        } catch (Exception e) {
            throw new LazyException(e);
        }
    }

    public T toValue(Fieldable fieldable) {
        try {
            return fromString(fieldable.stringValue());
        } catch (Exception e) {
            throw new LazyException(e);
        }
    }

    @Override
    protected Query newRange(String name, T lower, T upper, boolean minInclusive, boolean maxInclusive) throws Exception {
        return new TermRangeQuery(name, lower == null ? null : toString(lower), upper == null ? null : toString(upper), minInclusive, maxInclusive);
    }
}
