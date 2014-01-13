package com.googlecode.totallylazy;


public interface UnaryOperator<T> extends Function<T, T>, java.util.function.UnaryOperator<T> {
    class constructors {
        public static <T> UnaryOperator<T> unary(final Function<T, T> callable) {
            return new UnaryOperator<T>() {
                @Override
                public T call(T t) throws Exception {
                    return callable.call(t);
                }
            };
        }
    }
}
