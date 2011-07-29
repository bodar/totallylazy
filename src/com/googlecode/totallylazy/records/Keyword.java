package com.googlecode.totallylazy.records;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.GenericType;
import com.googlecode.totallylazy.Value;

public class Keyword<T> implements Callable1<Record, T>, GenericType<T>, Value<String> {
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
        return other instanceof Keyword && value().equalsIgnoreCase(((Keyword) other).value());
    }

    @Override
    public int hashCode() {
        return value().toLowerCase().hashCode();
    }

    public T call(Record record) throws Exception {
        return record.get(this);
    }

    @Override
    public String toString() {
        return name;
    }

    public Class<T> forClass() {
        return aClass;
    }

    public String fullyQualifiedName() {
        return fullyQualifiedName;
    }

    public String value() {
        return name;
    }

    // TODO Rename to 'as' somehow
    public Keyword<T> alias(Keyword<T> keyword) {
        return new Keyword(fullyQualifiedName, keyword.value(), aClass);

    }
}
