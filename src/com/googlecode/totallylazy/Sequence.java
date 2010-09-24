package com.googlecode.totallylazy;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;


public abstract class Sequence<T> implements Iterable<T> {
    public void foreach(Runnable1<T> runnable) {
        Sequences.foreach(this, runnable);
    }

    public <S> Sequence<S> map(final Callable1<T, S> callable) {
        return Sequences.map(this, callable);
    }

    public Sequence<T> filter(final Predicate<T> predicate) {
        return Sequences.filter(this, predicate);
    }

    public <S> Sequence<S> flatMap(final Callable1<T, Iterable<S>> callable) {
        return Sequences.flatMap(this, callable);
    }

    public T head() {
        return Sequences.head(this);
    }

    public Option<T> headOption() {
        return Sequences.headOption(this);
    }

    public Sequence<T> tail() {
        return Sequences.tail(this);
    }

    public <S> S foldLeft(final S seed, final Callable2<S, T, S> callable) {
        return Sequences.foldLeft(this, seed, callable);
    }

    public T reduceLeft(final Callable2<T, T, T> callable) {
        return Sequences.reduceLeft(this, callable);
    }

    public String toString() {
        return Sequences.toString(this);
    }

    public String toString(final String separator) {
        return Sequences.toString(this, separator);
    }

    public String toString(final String start, final String separator, final String end) {
        return Sequences.toString(this, start, separator, end);
    }

    public Set<T> union(final Iterable<T> other) {
        return Sequences.union(Sequences.sequence(this, other));
    }

    public boolean isEmpty() {
        return Sequences.isEmpty(this);
    }

    public List<T> toList() {
        return Sequences.toList(this);
    }

    public T[] toArray(Class<T> aClass) {
        return toList().toArray((T[]) Array.newInstance(aClass, 0));
    }

    public Sequence<T> remove(T t) {
        return Sequences.remove(this, t);
    }

    public int size() {
        return Sequences.size(this);
    }

    public Sequence<T> take(int count) {
        return Sequences.take(this, count);
    }

    public Sequence<T> takeWhile(Predicate<T> predicate) {
        return Sequences.takeWhile(this, predicate);
    }

    public Sequence<T> drop(int count) {
        return Sequences.drop(this, count);
    }

    public Sequence<T> dropWhile(Predicate<T> predicate) {
        return Sequences.dropWhile(this, predicate);
    }
}
