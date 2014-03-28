package com.googlecode.totallylazy;

public abstract class Mapper<T,R> implements Function<T,R> {
    public static <T,R> Mapper<T,R> mapper(final Function<? super T, ? extends R> callable){
        return new Mapper<T, R>() {
            @Override
            public R call(T t) throws Exception {
                return callable.call(t);
            }
        };
    }
}
