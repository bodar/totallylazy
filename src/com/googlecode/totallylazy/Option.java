package com.googlecode.totallylazy;

import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.Callers.call;

public abstract class Option<T> implements Iterable<T>, Value<T> {
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
        return none();
    }

    public abstract T get();

    public abstract boolean isEmpty();

    public T value() {
        return get();
    }

    public final T getOrElse(T other){
        return isEmpty() ? other : get();
    }

    public final T getOrElse(Callable<T> callable){
        return isEmpty() ? call(callable) : get();
    }

    public final T getOrNull(){
        return isEmpty() ? null : get();
    }

    public final <S> Option<S> map(Callable1<? super T, S> callable) {
        return isEmpty() ? Option.<S>none() : some(Callers.call(callable, get()));
    }

    public <S> S fold(final S seed, final Callable2<? super S, ? super T, S> callable) {
        return isEmpty() ? seed : Callers.call(callable, seed, get());
    }
}
