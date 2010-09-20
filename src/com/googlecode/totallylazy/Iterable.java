package com.googlecode.totallylazy;

public abstract class Iterable<T> implements java.lang.Iterable<T> {
    public void foreach(Runnable1<T> runnable) {
        Iterators.foreach(this.iterator(), runnable);
    }

    public <S> Iterable<S> map(final Callable1<T, S> callable) {
        return Iterables.map(this, callable);
    }

    public Iterable<T> filter(Predicate<T> predicate) {
        return Iterables.filter(this, predicate);
    }

    public <S> Iterable<S> flatMap(Callable1<T, Iterable<S>> callable) {
        return Iterables.flatMap(this, callable);
    }
}
