package com.googlecode.totallylazy;

public interface GenericType<T> {
    Class<T> forClass();

    public static final class functions {
        public static <T extends GenericType<?>> Function1<T, Class<?>> forClass() {
            return new Function1<T, Class<?>>() {
                @Override
                public Class<?> call(T genericType) throws Exception {
                    return genericType.forClass();
                }
            };
        }
    }
}
