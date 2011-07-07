package com.googlecode.totallylazy.records.lucene.mappings;

import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Fieldable;
import org.apache.lucene.document.NumericField;

import java.util.Date;

public class DateMapping implements Mapping<Date> {
    public Fieldable toField(String name, Object value) {
        return new NumericField(name, Field.Store.YES, true).setLongValue(((Date) value).getTime());
    }

    public Date toValue(Fieldable fieldable) {
        return new Date(((NumericField) fieldable).getNumericValue().longValue());
    }
}
