package com.googlecode.totallylazy;

public interface GenericType<T> {
    Class<T> forClass();

    public static final class functions {
        public static <T extends GenericType<?>> Function1<T, Class<?>> forClass() {
            return genericType -> genericType.forClass();
        }
    }
}
