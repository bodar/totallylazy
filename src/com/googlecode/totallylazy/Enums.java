package com.googlecode.totallylazy;

import java.util.Formatter;

public class Enums {
    public static <T extends Enum<T>> Callable1<T, String> name() {
        return new Callable1<T, String>() {
            public String call(T anEnum) throws Exception {
                return anEnum.name();
            }
        };
    }

    public static <T extends Enum<T>> Callable1<T, String> name(Class<T> aClass) {
        return name();
    }
}
