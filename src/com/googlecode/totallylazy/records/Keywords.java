package com.googlecode.totallylazy.records;

import com.googlecode.totallylazy.Callable1;

public class Keywords {
    public static Callable1<Keyword, String> name() {
        return new Callable1<Keyword, String>() {
            public String call(Keyword keyword) throws Exception {
                return keyword.name();
            }
        };
    }

    public static Keyword<Object> keyword(String value) {
        return new Keyword<Object>(value, value, Object.class);
    }

    public static <T> Keyword<T> keyword(String value, Class<T> aClass) {
        return new Keyword<T>(value, value, aClass);
    }

    public static Keyword<Object> keyword(String fullyQualifiedName, String name) {
        return new Keyword<Object>(fullyQualifiedName, name, Object.class);
    }

    public static <T> Keyword<T> keyword(String fullyQualifiedName, String name, Class<T> aClass) {
        return new Keyword<T>(fullyQualifiedName, name, aClass);
    }
}
