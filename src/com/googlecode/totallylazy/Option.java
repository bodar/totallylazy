package com.googlecode.totallylazy;

public abstract class Option<T> implements Iterable<T> {
    public static <T> Some<T> some(T t){
        return new Some<T>(t);
    }

    public static <T> None<T> none(Class<T> aClass){
        return new None<T>();
    }
}
