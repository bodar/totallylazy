package com.googlecode.totallylazy.records.simpledb.mappings;

public class LongMapping implements Mapping<Long>{
    public Long toValue(String value) {
        return Long.parseLong(value);
    }

    public String toString(Long value) {
        return Long.toString(value);
    }
}
