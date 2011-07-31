package com.googlecode.totallylazy.records;

public class ImmutableKeyword<T> extends AbstractKeyword<T> {
    public ImmutableKeyword(String name, Class<T> aClass) {
        super(name, aClass);
    }

    public AliasedKeyword<T> as(Keyword<T> keyword) {
        return new AliasedKeyword<T>(this, keyword.name());
    }
}
