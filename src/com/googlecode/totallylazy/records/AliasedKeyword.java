package com.googlecode.totallylazy.records;

public class AliasedKeyword<T> extends ImmutableKeyword<T>{
    private final Keyword<T> source;

    public AliasedKeyword(Keyword<T> source, String name) {
        super(name, source.forClass());
        this.source = source;
    }

    public Keyword<T> source() {
        return source;
    }
}
