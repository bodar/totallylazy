package com.googlecode.totallylazy.records;

public class AliasedKeyword<T> extends AbstractKeyword<T>{
    private final Keyword<T> source;
    private final String name;

    public AliasedKeyword(Keyword<T> source, String name) {
        this.source = source;
        this.name = name;
    }

    public Keyword source() {
        return source;
    }

    public String name() {
        return name;
    }

    public Class<T> forClass() {
        return source.forClass();
    }

}
