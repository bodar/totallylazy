package com.googlecode.totallylazy.records.lucene.mappings;

import org.apache.lucene.document.Field;
import org.apache.lucene.document.Fieldable;
import org.apache.lucene.document.NumericField;

public class IntegerMapping implements Mapping<Integer> {
    public Fieldable toField(String name, Object value) {
        return new NumericField(name, Field.Store.YES, true).setIntValue((Integer) value);
    }

    public Integer toValue(Fieldable fieldable) {
        return ((NumericField) fieldable).getNumericValue().intValue();
    }
}
