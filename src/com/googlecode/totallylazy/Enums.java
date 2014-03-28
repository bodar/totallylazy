package com.googlecode.totallylazy;

public class Enums {
    public static <T extends Enum<T>> Function1<T, String> name() {
        return new Function1<T, String>() {
            public String call(T anEnum) throws Exception {
                return anEnum.name();
            }
        };
    }

    public static <T extends Enum<T>> Function1<T, String> name(Class<T> aClass) {
        return name();
    }
}
