package com.googlecode.totallylazy;

import java.util.concurrent.Callable;

public class Callables {
    public static <T> Callable1<T, String> asString(Class<T> aClass) {
        return new Callable1<T, String>() {
            public String call(T value){
                return value.toString();
            }
        };
    }

    public static <T> Callable<T> returns(final T t) {
        return new Callable<T>() {
            public T call() throws Exception {
                return t;
            }
        };
    }

    public static <T> Callable<T> callThrows(final Exception e, Class<T> aClass) {
        return new Callable<T>() {
            public T call() throws Exception {
                throw e;
            }
        };
    }

    public static <T> Callable1<Callable<T>, T> call(Class<T> aClass) {
        return new Callable1<Callable<T>, T>() {
            public T call(Callable<T> callable) throws Exception {
                return callable.call();
            }
        };
    }

    public static Callable1<Integer, Integer> increment() {
        return new Callable1<Integer, Integer>(){
            public Integer call(final Integer integer) throws Exception {
                return integer + 1;
            }
        };
    }

}
