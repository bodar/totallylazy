package com.googlecode.totallylazy.records.simpledb.mappings;

public class IntegerMapping implements Mapping<Integer>{
    public Integer toValue(String value) {
        return Integer.parseInt(value);
    }

    public String toString(Integer value) {
        return Integer.toString(value);
    }
}
