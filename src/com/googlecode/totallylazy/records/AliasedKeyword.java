package com.googlecode.totallylazy.records;

public class AliasedKeyword<T> extends ImmutableKeyword<T>{
    private final Keyword source;

    public AliasedKeyword(Keyword source, String name) {
        super(name, source.forClass());
        this.source = source;
    }

    public Keyword source() {
        return source;
    }
}
