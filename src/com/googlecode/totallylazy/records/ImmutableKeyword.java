package com.googlecode.totallylazy.records;

public class ImmutableKeyword<T> implements Keyword<T>{
    private final String name;
    private final Class<T> aClass;

    public ImmutableKeyword(String name, Class<T> aClass) {
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

    public String value() {
        return name;
    }

    // TODO Rename to 'as' somehow
    public AliasedKeyword<T> alias(Keyword<T> keyword) {
        return new AliasedKeyword<T>(this, keyword.value());
    }
}
