package com.googlecode.totallylazy.records.lucene.mappings;

import org.apache.lucene.document.Field;

public class StringMapping extends AbstractStringMapping<String> {
    public StringMapping() {
        super(Field.Index.ANALYZED);
    }

    @Override
    public String toString(String value) throws Exception {
        return value;
    }

    @Override
    protected String fromString(String value) throws Exception {
        return value;
    }

}
