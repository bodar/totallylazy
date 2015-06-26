package com.googlecode.totallylazy;

import com.googlecode.totallylazy.functions.Function1;

public interface GenericType<T> {
    Class<T> forClass();

    public static final class functions {
        public static <T extends GenericType<?>> Function1<T, Class<?>> forClass() {
            return genericType -> genericType.forClass();
        }
    }
}
