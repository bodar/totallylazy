package com.googlecode.totallylazy.records;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.GenericType;

import java.util.concurrent.Callable;

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
        return other instanceof Keyword && name().equalsIgnoreCase(((Keyword) other).name());
    }

    @Override
    public int hashCode() {
        return name().toLowerCase().hashCode();
    }

    public T call(Record record) throws Exception {
        return record.get(this);
    }

    @Override
    public String toString() {
        return value;
    }

    public Class<T> forClass() {
        return aClass;
    }

    public String name() {
        String[] parts = value.split("\\.");
        return parts[parts.length - 1];
    }

}
