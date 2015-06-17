package com.googlecode.totallylazy;

public interface Unary<T> extends Function1<T, T> {
    class constructors {
        public static <T> UnaryFunction<T> unary(final Function1<T, T> callable) {
            return new UnaryFunction<T>() {
                @Override
                public T call(T t) throws Exception {
                    return callable.call(t);
                }
            };
        }
    }
}
