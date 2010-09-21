package com.googlecode.totallylazy;

import com.googlecode.totallylazy.iterators.*;

import java.util.NoSuchElementException;

public class Iterators {
    public static <T> void foreach(java.util.Iterator<T> iterator, Runnable1<T> runnable) {
        while (iterator.hasNext()) {
            runnable.run(iterator.next());
        }
    }

    public static <T, S> Iterator<S> map(java.util.Iterator<T> iterator, Callable1<T, S> callable) {
        return new MapIterator<T, S>(iterator, callable);
    }

    public static <T, S> Iterator<S> flatMap(java.util.Iterator<T> iterator, Callable1<T, java.lang.Iterable<S>> callable) {
        return new FlatMapIterator<T, S>(iterator, callable);
    }

    public static <T> Iterator<T> filter(java.util.Iterator<T> iterator, Predicate<T> predicate) {
        return new FilterIterator<T>(iterator, predicate);
    }


    public static <T> Iterator<T> iterate(final Callable1<T, T> callable, T t) {
        return new IterateIterator<T>(callable, t);
    }

    public static <T> T head(java.util.Iterator<T> iterator) {
        if (iterator.hasNext()) {
            return iterator.next();
        }
        throw new NoSuchElementException();
    }

    public static <T> Iterator<T> tail(final java.util.Iterator<T> iterator) {
        if (iterator.hasNext()) {
            iterator.next();
            return new DelegatingIterator<T>(iterator);
        }
        throw new NoSuchElementException();
    }

    public static <T, S> S foldLeft(java.util.Iterator<T> iterator, S seed, Callable2<S, T, S> callable) {
        S accumilator = seed;
        while (iterator.hasNext()) {
            try {
                accumilator = callable.call(accumilator, iterator.next());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return accumilator;
    }

    public static <T> T reduceLeft(java.util.Iterator<T> iterator, Callable2<T, T, T> callable) {
        return foldLeft(iterator, iterator.next(), callable);
    }

    public static String toString(java.util.Iterator iterator) {
        return toString(iterator, "");
    }

    public static String toString(java.util.Iterator iterator, String separator) {
        return toString(iterator, "", separator, "");
    }

    public static String toString(java.util.Iterator iterator, String start, String separator, String end) {
        StringBuilder builder = new StringBuilder();
        builder.append(start);
        if (iterator.hasNext()) builder.append(iterator.next());
        while (iterator.hasNext()) {
            builder.append(separator);
            builder.append(iterator.next());
        }
        builder.append(end);
        return builder.toString();
    }
}
