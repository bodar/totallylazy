package com.googlecode.totallylazy.records;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Sequence;

public class Keywords {

    public static final Keyword<Boolean> UNIQUE = Keywords.keyword("unique", Boolean.class);
    public static final Keyword<Boolean> INDEXED = Keywords.keyword("indexed", Boolean.class);

    public static Callable1<Keyword, String> name() {
        return new Callable1<Keyword, String>() {
            public String call(Keyword keyword) throws Exception {
                return keyword.name();
            }
        };
    }

    public static ImmutableKeyword<Object> keyword(String value) {
        return new ImmutableKeyword<Object>(value, Object.class);
    }

    public static <T> ImmutableKeyword<T> keyword(String value, Class<T> aClass) {
        return new ImmutableKeyword<T>(value, aClass);
    }

    public static boolean equalto(Keyword keyword, Keyword other) {
        return keyword.name().equalsIgnoreCase(other.name());
    }

    public static <T> Callable1<? super Keyword, T> metadata(final Keyword<T> metadataKey) {
        return new Callable1<Keyword, T>() {
            public T call(Keyword keyword) throws Exception {
                return keyword.metadata().get(metadataKey);
            }
        };
    }

    public static Sequence<Keyword> keywords(Sequence<Record> results) {
        return results.flatMap(keywords()).unique();
    }

    public static Callable1<? super Record, Sequence<Keyword>> keywords() {
        return new Callable1<Record, Sequence<Keyword>>() {
            public Sequence<Keyword> call(Record record) throws Exception {
                return record.keywords();
            }
        };
    }
}
