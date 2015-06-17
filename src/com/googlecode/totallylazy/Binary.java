package com.googlecode.totallylazy;

public interface Binary<T> extends Function2<T, T, T> {
    class constructors {
        public static <T> BinaryFunction<T> binary(final Function2<T,T,T> callable) {
            return new BinaryFunction<T>() {
                @Override
                public T call(T t, T t2) throws Exception {
                    return callable.call(t, t2);
                }
            };
        }
    }
}