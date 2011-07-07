package com.googlecode.totallylazy.records.lucene.mappings;

import org.apache.lucene.document.Field;
import org.apache.lucene.document.Fieldable;

public class StringMapping implements Mapping<String> {
    public Fieldable toField(String name, Object value) {
        return new Field(name, ((String) value), Field.Store.YES, Field.Index.NOT_ANALYZED);
    }

    public String toValue(Fieldable fieldable) {
        return fieldable.stringValue();
    }
}
