package com.googlecode.totallylazy;

import com.googlecode.totallylazy.iterators.ArrayIterator;
import com.googlecode.totallylazy.iterators.CharacterIterator;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class Sequences {
    public static <T> Sequence<T> sequence(final Iterable<T> iterable){
        return new Sequence<T>() {
            public Iterator<T> iterator() {
                return iterable.iterator();
            }
        };
    }

    public static <T> Sequence<T> sequence(final T ... items){
        return new Sequence<T>() {
            public Iterator<T> iterator() {
                return new ArrayIterator<T>(items);
            }
        };
    }

    public static Sequence<Character> characters(final String value){
        return new Sequence<Character>() {
            public Iterator<Character> iterator() {
                return new CharacterIterator(value.toCharArray());
            }
        };
    }

    public static <T, S> Sequence<S> map(final Iterable<T> iterable, final Callable1<T,S> callable) {
        return new Sequence<S>() {
            public Iterator<S> iterator() {
                return Iterators.map(iterable.iterator(), callable);
            }
        };
    }

    public static <T> Sequence<T> filter(final Iterable<T> iterable, final Predicate<T> predicate) {
        return new Sequence<T>() {
            public Iterator<T> iterator() {
                return Iterators.filter(iterable.iterator(), predicate);
            }
        };
    }

    public static <T,S> Sequence<S> flatMap(final Iterable<T> iterable, final Callable1<T, Iterable<S>> callable) {
        return new Sequence<S>() {
            public Iterator<S> iterator() {
                return Iterators.flatMap(iterable.iterator(), callable);
            }
        };
    }

    public static <T> Sequence<T> iterate(final Callable1<T, T> callable, final T t) {
        return new Sequence<T>() {
            public Iterator<T> iterator() {
                return Iterators.iterate(callable, t);
            }
        };
    }

    public static Sequence<Integer> range(final int end) {
        return new Sequence<Integer>() {
            public Iterator<Integer> iterator() {
                return Iterators.range(end);
            }
        };
    }

    public static Sequence<Integer> range(final int start, final int end) {
        return new Sequence<Integer>() {
            public Iterator<Integer> iterator() {
                return Iterators.range(start, end);
            }
        };
    }

    public static Sequence<Integer> range(final int start, final int end, final int step) {
        return new Sequence<Integer>() {
            public Iterator<Integer> iterator() {
                return Iterators.range(start, end, step);
            }
        };
    }

    public static <T> void foreach(final Iterable<T> iterable, final Runnable1<T> runnable) {
        Iterators.foreach(iterable.iterator(), runnable);
    }

    public static <T> T head(final Iterable<T> iterable) {
        return Iterators.head(iterable.iterator());
    }

    public static <T> Option<T> headOption(final Iterable<T> iterable) {
        return Iterators.headOption(iterable.iterator());
    }

    public static <T> Sequence<T> tail(final Iterable<T> iterable) {
        return new Sequence<T>() {
            public Iterator<T> iterator() {
                return Iterators.tail(iterable.iterator());
            }
        };
    }

    public static <T, S> S foldLeft(final Iterable<T> iterable, S seed, Callable2<S,T,S> callable) {
        return Iterators.foldLeft(iterable.iterator(), seed, callable);
    }

    public static <T> T reduceLeft(final Iterable<T> iterable, Callable2<T,T,T> callable) {
        return Iterators.reduceLeft(iterable.iterator(), callable);
    }
    public static String toString(final Iterable iterable) {
        return Iterators.toString(iterable.iterator());
    }
    public static String toString(final Iterable iterable, String separator) {
        return Iterators.toString(iterable.iterator(), separator);
    }

    public static String toString(final Iterable iterable, String start, String separator, String end) {
        return Iterators.toString(iterable.iterator(), start, separator, end);
    }

    public static <T> Set<T> union(final Iterable<Iterable<T>> iterables) {
        return Iterators.union(map(iterables, Callables.<T>asIterator()));
    }

    public static boolean isEmpty(final Iterable iterable) {
        return !iterable.iterator().hasNext();
    }

    public static <T> List<T> toList(final Iterable<T> iterable) {
        return Iterators.toList(iterable.iterator());
    }

    public static <T> Sequence<T> remove(final Iterable<T> iterable, final T t) {
        return new Sequence<T>() {
            public Iterator<T> iterator() {
                return Iterators.remove(iterable.iterator(), t);
            }
        };
    }

    public static <T> int size(final Iterable<T> iterable) {
           return sequence(iterable).toList().size();
    }

    public static <T> Sequence<T> take(final Iterable<T> iterable, final int count) {
        return new Sequence<T>() {
            public Iterator<T> iterator() {
                return Iterators.take(iterable.iterator(), count);
            }
        };
    }

    public static <T> Sequence<T> takeWhile(final Iterable<T> iterable, final Predicate<T> predicate) {
        return new Sequence<T>() {
            public Iterator<T> iterator() {
                return Iterators.takeWhile(iterable.iterator(), predicate);
            }
        };
    }

    public static <T> Sequence<T> drop(final Iterable<T> iterable, final int count) {
        return new Sequence<T>() {
            public Iterator<T> iterator() {
                return Iterators.drop(iterable.iterator(), count);
            }
        };
    }

    public static <T> Sequence<T> dropWhile(final Iterable<T> iterable, final Predicate<T> predicate) {
        return new Sequence<T>() {
            public Iterator<T> iterator() {
                return Iterators.dropWhile(iterable.iterator(), predicate);
            }
        };
    }

}
