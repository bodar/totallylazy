package com.googlecode.totallylazy;

import java.util.*;

import static java.util.Arrays.asList;

public abstract class Iterable<T> implements java.lang.Iterable<T> {
    public static <T> Iterable<T> list(final T ... items){
        return new Iterable<T>() {
            public java.util.Iterator<T> iterator() {
                return asList(items).iterator();
            }
        };
    }
    
    public void foreach(Runnable1<T> runnable) {
        Iterator.foreach(this.iterator(), runnable);
    }

    public static <T> void foreach(java.lang.Iterable<T> iterable, Runnable1<T> runnable){
        Iterator.foreach(iterable.iterator(), runnable);
    }

    public <S> Iterable<S> map(final Callable1<T, S> callable) {
        return map(this, callable);
    }

    public static <T, S> Iterable<S> map(final Iterable<T> iterable, final Callable1<T,S> callable) {
        return new Iterable<S>() {
            public java.util.Iterator<S> iterator() {
                return Iterator.map(iterable.iterator(), callable);
            }
        };
    }

    public Iterable<T> filter(Predicate<T> predicate) {
        return filter(this, predicate);
    }

    public static <T> Iterable<T> filter(final Iterable<T> iterable, final Predicate<T> predicate) {
        return new Iterable<T>() {
            public Iterator<T> iterator() {
                return Iterator.filter(iterable.iterator(), predicate);
            }
        };
    }

    public <S> Iterable<S> flatMap(Callable1<T, Iterable<S>> callable) {
        return flatMap(this, callable);
    }

    public static <T,S> Iterable<S> flatMap(final Iterable<T> iterable, final Callable1<T, Iterable<S>> callable) {
        return new Iterable<S>() {
            public java.util.Iterator<S> iterator() {
                return Iterator.flaMap(iterable.iterator(), callable);
            }
        };
    }
}
