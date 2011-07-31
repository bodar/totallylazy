package com.googlecode.totallylazy.records;

public abstract class AbstractKeyword<T> implements Keyword<T> {
    protected final String name;
    protected final Class<T> aClass;

    public AbstractKeyword(String name, Class<T> aClass) {
        if(name == null){
            throw new IllegalArgumentException("name");
        }
        this.name = name;
        this.aClass = aClass;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Keyword && Keywords.equalto(this, (Keyword) other);
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
        return name;
    }

    public Class<T> forClass() {
        return aClass;
    }

    public String name() {
        return name;
    }
}
