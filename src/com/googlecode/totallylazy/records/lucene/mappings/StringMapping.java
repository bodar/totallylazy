package com.googlecode.totallylazy.records.lucene.mappings;

import com.googlecode.totallylazy.LazyException;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Fieldable;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermRangeQuery;

import java.text.ParseException;

public abstract class StringMapping<T> implements Mapping<T> {
    protected abstract String toString(T value) throws Exception;

    protected abstract T fromString(final String value) throws Exception;

    public Fieldable toField(String name, T value) {
        try {
            return new Field(name, toString(value), Field.Store.YES, Field.Index.NOT_ANALYZED);
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

    public Query equalTo(String name, T value) {
        try {
            return new TermQuery(new Term(name, toString(value)));
        } catch (Exception e) {
            throw new LazyException(e);
        }
    }

    public Query greaterThan(String name, T value) {
        try {
            return new TermRangeQuery(name, toString(value), null, false, true);
        } catch (Exception e) {
            throw new LazyException(e);
        }
    }
}
