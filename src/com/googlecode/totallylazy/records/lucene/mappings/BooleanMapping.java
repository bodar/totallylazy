package com.googlecode.totallylazy.records.lucene.mappings;

import org.apache.lucene.document.Field;

import java.text.ParseException;

public class BooleanMapping extends AbstractStringMapping<Boolean> {
    public BooleanMapping() {
        super(Field.Index.NOT_ANALYZED);
    }

    @Override
    protected String toString(Boolean value) {
        return Boolean.toString(value);
    }

    @Override
    protected Boolean fromString(String value) throws ParseException {
        return Boolean.parseBoolean(value);
    }
}
