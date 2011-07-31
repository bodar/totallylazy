package com.googlecode.totallylazy.records;

public class ImmutableKeyword<T> extends AbstractKeyword<T> {
    private final String name;
    private final Class<T> aClass;

    public ImmutableKeyword(String name, Class<T> aClass) {
        if(name == null){
            throw new IllegalArgumentException("name");
        }
        this.name = name;
        this.aClass = aClass;
    }


    public AliasedKeyword<T> as(Keyword<T> keyword) {
        return new AliasedKeyword<T>(this, keyword.name());
    }

    public String name() {
        return name;
    }

    public Class<T> forClass() {
        return aClass;
    }
}
