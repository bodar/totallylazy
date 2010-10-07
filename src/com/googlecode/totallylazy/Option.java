package com.googlecode.totallylazy;

import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.Callers.call;

public abstract class Option<T> implements Iterable<T> {
    public static <T> Option<T> option(T t) {
        if (t == null) {
            return none();
        }
        return new Some<T>(t);
    }

    public static <T> Some<T> some(T t) {
        return new Some<T>(t);
    }

    public static <T> None<T> none() {
        return new None<T>();
    }

    public static <T> None<T> none(Class<T> aClass) {
        return new None<T>();
    }

    public abstract T get();

    public abstract boolean isEmpty();

    public T getOrElse(T other){
        return isEmpty() ? other : get();
    }

    public T getOrElse(Callable<T> callable){
        return isEmpty() ? call(callable) : get();
    }

    public T getOrNull(){
        return isEmpty() ? null : get();
    }

    public <S> Option<S> map(Callable1<? super T, S> callable) {
        return isEmpty() ? Option.<S>none() : some(Callers.call(callable, get()));
    }
}
