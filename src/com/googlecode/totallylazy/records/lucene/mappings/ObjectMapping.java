package com.googlecode.totallylazy.records.lucene.mappings;

import org.apache.lucene.document.Field;

public class ObjectMapping extends AbstractStringMapping<Object> {
    public ObjectMapping() {
        super(Field.Index.NOT_ANALYZED);
    }

    @Override
    public String toString(Object value) throws Exception {
        return value.toString();
    }

    @Override
    protected Object fromString(String value) throws Exception {
        return value;
    }

}
