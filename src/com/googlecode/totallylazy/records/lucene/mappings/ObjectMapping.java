package com.googlecode.totallylazy.records.lucene.mappings;

import org.apache.lucene.document.Field;
import org.apache.lucene.document.Fieldable;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;

public class ObjectMapping implements Mapping<Object> {
    public Fieldable toField(String name, Object value) {
        return new Field(name, value.toString(), Field.Store.YES, Field.Index.NOT_ANALYZED);
    }

    public String toValue(Fieldable fieldable) {
        return fieldable.stringValue();
    }

    public Query equalTo(String name, Object value) {
        return new TermQuery(new Term(name, value.toString()));
    }
}
