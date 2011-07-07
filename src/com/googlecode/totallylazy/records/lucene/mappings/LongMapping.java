package com.googlecode.totallylazy.records.lucene.mappings;

import org.apache.lucene.document.Fieldable;

public class LongMapping implements Mapping<Long> {
    public Fieldable toField(String name, Object value) {
        throw new UnsupportedOperationException();
    }

    public Long toValue(Fieldable longClass) {
        throw new UnsupportedOperationException();
    }
}
