package com.googlecode.totallylazy.records;

import com.googlecode.totallylazy.Callable1;

public class Keywords {
    public static Callable1<Keyword, String> name() {
        return new Callable1<Keyword, String>() {
            public String call(Keyword keyword) throws Exception {
                return keyword.value();
            }
        };
    }

    public static Keyword<Object> keyword(String value) {
        return new ImmutableKeyword<Object>(value, Object.class);
    }

    public static <T> Keyword<T> keyword(String value, Class<T> aClass) {
        return new ImmutableKeyword<T>(value, aClass);
    }

    public static Keyword<Object> keyword(String fullyQualifiedName, String name) {
        return new ImmutableKeyword<Object>(name, Object.class);
    }

    public static <T> Keyword<T> keyword(String fullyQualifiedName, String name, Class<T> aClass) {
        return new ImmutableKeyword<T>(name, aClass);
    }

    public static boolean equalto(Keyword keyword, Keyword other) {
        return keyword.value().equalsIgnoreCase(other.value());
    }
}
