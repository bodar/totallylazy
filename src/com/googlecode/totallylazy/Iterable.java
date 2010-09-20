package com.googlecode.totallylazy;

public abstract class Iterable<T> implements java.lang.Iterable<T> {
    public void foreach(Runnable1<T> runnable) {
        Iterables.foreach(this, runnable);
    }

    public <S> Iterable<S> map(final Callable1<T, S> callable) {
        return Iterables.map(this, callable);
    }

    public Iterable<T> filter(Predicate<T> predicate) {
        return Iterables.filter(this, predicate);
    }

    public <S> Iterable<S> flatMap(Callable1<T, java.lang.Iterable<S>> callable) {
        return Iterables.flatMap(this, callable);
    }

    public T head() {
        return Iterables.head(this);
    }

    public Iterable<T> tail() {
        return Iterables.tail(this);
    }

    public <S> S foldLeft(S seed, Callable2<S, T, S> callable) {
        return Iterables.foldLeft(this, seed, callable);
    }

    public T reduceLeft(Callable2<T, T, T> callable) {
        return Iterables.reduceLeft(this, callable);
    }
}
