package com.googlecode.totallylazy;

public interface GenericType<T> {
    Class<T> forClass();

    public static final class functions {
        public static <T extends GenericType<?>> Function<T, Class<?>> forClass() {
            return new Function<T, Class<?>>() {
                @Override
                public Class<?> call(T genericType) throws Exception {
                    return genericType.forClass();
                }
            };
        }
    }
}
