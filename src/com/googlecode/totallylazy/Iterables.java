package com.googlecode.totallylazy;

import static java.util.Arrays.asList;

public class Iterables {
    public static <T> Iterable<T> list(final T ... items){
        return new Iterable<T>() {
            public java.util.Iterator<T> iterator() {
                return asList(items).iterator();
            }
        };
    }

    public static <T, S> Iterable<S> map(final Iterable<T> iterable, final Callable1<T,S> callable) {
        return new Iterable<S>() {
            public java.util.Iterator<S> iterator() {
                return Iterators.map(iterable.iterator(), callable);
            }
        };
    }

    public static <T> Iterable<T> filter(final Iterable<T> iterable, final Predicate<T> predicate) {
        return new Iterable<T>() {
            public Iterator<T> iterator() {
                return Iterators.filter(iterable.iterator(), predicate);
            }
        };
    }

    public static <T,S> Iterable<S> flatMap(final Iterable<T> iterable, final Callable1<T, Iterable<S>> callable) {
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


}
