package com.googlecode.totallylazy;

import java.util.Set;

import static java.util.Arrays.asList;

public class Iterables {
    public static <T> Iterable<T> iterable(final java.lang.Iterable<T> iterable){
        return new Iterable<T>() {
            public java.util.Iterator<T> iterator() {
                return iterable.iterator();
            }
        };
    }

    public static <T> Iterable<T> list(final T ... items){
        return new Iterable<T>() {
            public java.util.Iterator<T> iterator() {
                return asList(items).iterator();
            }
        };
    }

    public static <T, S> Iterable<S> map(final java.lang.Iterable<T> iterable, final Callable1<T,S> callable) {
        return new Iterable<S>() {
            public java.util.Iterator<S> iterator() {
                return Iterators.map(iterable.iterator(), callable);
            }
        };
    }

    public static <T> Iterable<T> filter(final java.lang.Iterable<T> iterable, final Predicate<T> predicate) {
        return new Iterable<T>() {
            public Iterator<T> iterator() {
                return Iterators.filter(iterable.iterator(), predicate);
            }
        };
    }

    public static <T,S> Iterable<S> flatMap(final java.lang.Iterable<T> iterable, final Callable1<T, java.lang.Iterable<S>> callable) {
        return new Iterable<S>() {
            public java.util.Iterator<S> iterator() {
                return Iterators.flatMap(iterable.iterator(), callable);
            }
        };
    }

    public static <T> Iterable<T> iterate(final Callable1<T, T> callable, final T t) {
        return new Iterable<T>() {
            public java.util.Iterator<T> iterator() {
                return Iterators.iterate(callable, t);
            }
        };
    }


    public static <T> void foreach(final java.lang.Iterable<T> iterable, final Runnable1<T> runnable) {
        Iterators.foreach(iterable.iterator(), runnable);
    }

    public static <T> T head(final java.lang.Iterable<T> iterable) {
        return Iterators.head(iterable.iterator());
    }

    public static <T> Iterable<T> tail(final java.lang.Iterable<T> iterable) {
        return new Iterable<T>() {
            public Iterator<T> iterator() {
                return Iterators.tail(iterable.iterator());
            }
        };
    }

    public static <T, S> S foldLeft(final java.lang.Iterable<T> iterable, S seed, Callable2<S,T,S> callable) {
        return Iterators.foldLeft(iterable.iterator(), seed, callable);
    }

    public static <T> T reduceLeft(final java.lang.Iterable<T> iterable, Callable2<T,T,T> callable) {
        return Iterators.reduceLeft(iterable.iterator(), callable);
    }

    public static String toString(final java.lang.Iterable iterable) {
        return Iterators.toString(iterable.iterator());
    }
    public static String toString(final java.lang.Iterable iterable, String separator) {
        return Iterators.toString(iterable.iterator(), separator);
    }
    public static String toString(final java.lang.Iterable iterable, String start, String separator, String end) {
        return Iterators.toString(iterable.iterator(), start, separator, end);
    }

    public static <T> Set<T> union(final java.lang.Iterable<java.lang.Iterable<T>> iterables) {
        return Iterators.union(map(iterables, Callables.<T>asIterator()));
    }

}
