package com.googlecode.totallylazy;

import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;


public abstract class LazyIterable<T> implements Iterable<T> {
    public void foreach(Runnable1<T> runnable) {
        Iterables.foreach(this, runnable);
    }

    public <S> LazyIterable<S> map(final Callable1<T, S> callable) {
        return Iterables.map(this, callable);
    }

    public LazyIterable<T> filter(final Predicate<T> predicate) {
        return Iterables.filter(this, predicate);
    }

    public <S> LazyIterable<S> flatMap(final Callable1<T, Iterable<S>> callable) {
        return Iterables.flatMap(this, callable);
    }

    public T head() {
        return Iterables.head(this);
    }

    public LazyIterable<T> tail() {
        return Iterables.tail(this);
    }

    public <S> S foldLeft(final S seed, final Callable2<S, T, S> callable) {
        return Iterables.foldLeft(this, seed, callable);
    }

    public T reduceLeft(final Callable2<T, T, T> callable) {
        return Iterables.reduceLeft(this, callable);
    }

    public String toString() {
        return Iterables.toString(this);
    }

    public String toString(final String separator) {
        return Iterables.toString(this, separator);
    }

    public String toString(final String start, final String separator, final String end) {
        return Iterables.toString(this, start, separator, end);
    }

    public Set<T> union(final Iterable<T> other) {
        return Iterables.union(Iterables.list(this, other));
    }

    public boolean isEmpty() {
        return Iterables.isEmpty(this);
    }

    public List<T> toList() {
        return Iterables.toList(this);
    }

    public T[] toArray(Class<T> aClass) {
        T[] array = (T[]) Array.newInstance(aClass, 0);
        return toList().toArray(array);
    }
}
