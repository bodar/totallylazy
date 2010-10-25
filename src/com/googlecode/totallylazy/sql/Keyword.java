package com.googlecode.totallylazy.sql;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.GenericType;

public class Keyword<T> implements Callable1<Record, T>, GenericType {
    private final String value;
    private final Class<T> aClass;

    private Keyword(String value, Class<T> aClass) {
        this.value = value;
        this.aClass = aClass;
    }
    
    public static Keyword<Object> keyword(String value) {
        return new Keyword<Object>(value, Object.class);
    }

    public static <T> Keyword<T> keyword(String value, Class<T> aClass) {
        if(value == null){
            throw new IllegalArgumentException("value");
        }
        return new Keyword<T>(value, aClass);
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Keyword && value.equals(((Keyword) other).value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    public T call(Record record) throws Exception {
        return record.get(this);
    }

    @Override
    public String toString() {
        return value;
    }

    public Class forClass() {
        return aClass;
    }
}
