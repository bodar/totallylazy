package com.googlecode.totallylazy;

import java.util.Iterator;
import java.util.concurrent.Callable;

public class Callables {
    public static <Object> Callable1<Object, String> asString() {
        return new Callable1<Object, String>() {
            public String call(Object value){
                return value.toString();
            }
        };
    }

    public static <T> Callable1<Iterable<T>, Iterator<T>> asIterator() {
        return new Callable1<Iterable<T>, Iterator<T>>() {
            public Iterator<T> call(Iterable<T> iterable) throws Exception {
                return iterable.iterator();
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

    public static <T> Callable<T> aNull(Class<T> aClass) {
        return new Callable<T>() {
            public T call() throws Exception {
                return null;
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

    public static <T> Callable1<Callable<T>, T> invoke(Class<T> aClass) {
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

    public static Callable2<Integer, Integer, Integer> add() {
        return new Callable2<Integer, Integer, Integer>() {
            public Integer call(Integer a, Integer b){
                return a + b;
            }
        };
    }
}
