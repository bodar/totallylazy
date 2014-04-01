package com.googlecode.totallylazy;

public class Enums {
    public static <T extends Enum<T>> Function<T, String> name() {
        return new Function<T, String>() {
            public String call(T anEnum) throws Exception {
                return anEnum.name();
            }
        };
    }

    public static <T extends Enum<T>> Function<T, String> name(Class<T> aClass) {
        return name();
    }
}
