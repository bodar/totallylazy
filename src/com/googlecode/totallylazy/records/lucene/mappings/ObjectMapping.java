package com.googlecode.totallylazy.records.lucene.mappings;

public class ObjectMapping extends StringMapping<Object> {
    @Override
    public String toString(Object value) throws Exception {
        return value.toString();
    }

    @Override
    protected Object fromString(String value) throws Exception {
        return value;
    }

}
