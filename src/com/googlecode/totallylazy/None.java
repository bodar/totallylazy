package com.googlecode.totallylazy;

import com.googlecode.totallylazy.iterators.EmptyIterator;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.Callers.call;
import static com.googlecode.totallylazy.Unchecked.cast;

public class None<T> extends Option<T>{
    private static final None NONE = new None();

    private None() {}

    public static <T> None<T> none() {
        return cast(NONE);
    }

    public static <T> None<T> none(Class<T> aClass) {
        return none();
    }

    public Iterator<T> iterator() {
        return new EmptyIterator<T>();
    }

    @Override
    public T get() {
        throw new NoSuchElementException();
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public T getOrElse(T other) {
        return other;
    }

    @Override
    public T getOrElse(Callable<? extends T> callable) {
        return call(callable);
    }

    @Override
    public T getOrNull() {
        return null;
    }

    @Override
    public <S> Option<S> map(Callable1<? super T, ? extends S> callable) {
        return none();
    }

    @Override
    public <S> Option<S> flatMap(Callable1<? super T, ? extends Option<? extends S>> callable) {
        return none();
    }

    @Override
    public Option<T> filter(Predicate<? super T> predicate) {
        return this;
    }

    @Override
    public <S> S fold(S seed, Callable2<? super S, ? super T, ? extends S> callable) {
        return seed;
    }

    @Override
    public String toString() {
        return "none()";
    }
}