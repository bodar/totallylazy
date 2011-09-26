package com.googlecode.totallylazy.comparators;

import com.googlecode.totallylazy.Callable1;

import java.util.Comparator;

import static com.googlecode.totallylazy.Callers.call;

public class Comparators {
    public static <T, R> Comparator<? super T> where(final Callable1<T, R> callable, final Comparator<R> comparator) {
        return new Comparator<T>() {
            public int compare(T instance, T otherInstance) {
                return comparator.compare(call(callable, instance), call(callable, otherInstance));
            }
        };
    }

    public static <T extends Comparable<? super T>> Comparator<? super T> ascending() {
        return new Comparator<T>() {
            public int compare(T a, T b) {
                return a.compareTo(b);
            }
        };
    }

    public static <T extends Comparable<? super T>> Comparator<? super T> descending() {
        return new Comparator<T>() {
            public int compare(T a, T b) {
                return b.compareTo(a);
            }
        };
    }
}
