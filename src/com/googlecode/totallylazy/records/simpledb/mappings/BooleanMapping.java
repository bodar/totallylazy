package com.googlecode.totallylazy.records.simpledb.mappings;

public class BooleanMapping implements Mapping<Boolean>{
    public Boolean toValue(String value) {
        return Boolean.parseBoolean(value);
    }

    public String toString(Boolean value) {
        return Boolean.toString(value);
    }
}
