package com.googlecode.totallylazy.records;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.GenericType;

public class Keyword<T> implements Callable1<Record, T>, GenericType {
    private final String fullyQualifiedName;
    private final String name;
    private final Class<T> aClass;

    public Keyword(String fullyQualifiedName, String name, Class<T> aClass) {
        if(fullyQualifiedName == null){
            throw new IllegalArgumentException("fullyQualifiedName");
        }
        if(name == null){
            throw new IllegalArgumentException("name");
        }
        this.fullyQualifiedName = fullyQualifiedName;
        this.name = name;
        this.aClass = aClass;
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
        return fullyQualifiedName;
    }

    public Class<T> forClass() {
        return aClass;
    }

    public String fullyQualifiedName() {
        return fullyQualifiedName;
    }

    public String name() {
        return name;
    }

    public Keyword<T> alias(Keyword<T> keyword) {
        return new Keyword(fullyQualifiedName, keyword.name(), aClass);

    }
}
