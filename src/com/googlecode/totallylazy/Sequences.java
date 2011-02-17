package com.googlecode.totallylazy;

import static com.googlecode.totallylazy.Callables.ascending;
import com.googlecode.totallylazy.iterators.*;

import static com.googlecode.totallylazy.Predicates.not;
import static com.googlecode.totallylazy.numbers.Numbers.increment;

import static java.nio.CharBuffer.wrap;
import java.util.*;
import java.util.concurrent.Callable;

public class Sequences {
    public static <T> Sequence<T> empty() {
        return new Sequence<T>() {
            public final Iterator<T> iterator() {
                return new EmptyIterator<T>();
            }
        };
    }

    public static <T> Sequence<T> sequence(final Iterable<T> iterable) {
        if (iterable instanceof Sequence) {
            return (Sequence<T>) iterable;
        }

        return new Sequence<T>() {
            public final Iterator<T> iterator() {
                return iterable.iterator();
            }
        };
    }

    public static <T> Sequence<T> sequence(final T... items) {
        return new Sequence<T>() {
            public final Iterator<T> iterator() {
                return new ArrayIterator<T>(items);
            }
        };
    }

    public static <T> Sequence<T> sequence(final Enumeration<T> enumeration) {
        return new Sequence<T>() {
            public final Iterator<T> iterator() {
                return new EnumerationIterator<T>(enumeration);
            }
        };
    }

    public static <T> Sequence<T> sequence(final Enumeration enumeration, final Class<T> aClass) {
        return new Sequence<T>() {
            public final Iterator<T> iterator() {
                return new EnumerationIterator<T>(enumeration);
            }
        };
    }

    public static <T> Sequence<T> memorise(final Iterator<T> iterator) {
        return new MemorisedSequence<T>(new Iterable<T>() {
            public final Iterator<T> iterator() {
                return iterator;
            }
        });
    }

    public static <T> Sequence<T> forwardOnly(final Iterator<T> iterator) {
        return new Sequence<T>() {
            public final Iterator<T> iterator() {
                return iterator;
            }
        };
    }

    public static <T> Sequence<T> forwardOnly(Iterable<T> iterable) {
        return forwardOnly(iterable.iterator());
    }


    public static Sequence<Character> characters(final CharSequence value) {
        return new Sequence<Character>() {
            public final Iterator<Character> iterator() {
                return new CharacterIterator(value);
            }
        };
    }

    public static Sequence<Character> characters(final char[] value) {
        return characters(wrap(value));
    }

    public static <T, S> Sequence<S> map(final Iterable<T> iterable, final Callable1<? super T, S> callable) {
        return new Sequence<S>() {
            public final Iterator<S> iterator() {
                return Iterators.map(iterable.iterator(), callable);
            }
        };
    }

    public static <T> Pair<Sequence<T>, Sequence<T>> partition(final Iterable<T> iterable, Predicate<? super T> predicate) {
        Sequence<T> sequence = sequence(iterable).memorise();
        return Pair.pair(filter(sequence, predicate), filter(sequence, not(predicate)));
    }

    public static <T> Sequence<T> filter(final Iterable<T> iterable, final Predicate<? super T> predicate) {
        return new Sequence<T>() {
            public final Iterator<T> iterator() {
                return Iterators.filter(iterable.iterator(), predicate);
            }
        };
    }

    public static <T, S> Sequence<S> flatMap(final Iterable<T> iterable, final Callable1<? super T, Iterable<? extends S>> callable) {
        return new Sequence<S>() {
            public final Iterator<S> iterator() {
                return Iterators.flatMap(iterable.iterator(), callable);
            }
        };
    }

    public static <T> Sequence<T> iterate(final Callable1<? super T, T> callable, final T t) {
        return new Sequence<T>() {
            public final Iterator<T> iterator() {
                return Iterators.iterate(callable, t);
            }
        };
    }

    public static <T> Sequence<T> repeat(final Callable<T> callable) {
        return new Sequence<T>() {
            public final Iterator<T> iterator() {
                return Iterators.repeat(callable);
            }
        };
    }

    public static <T> Sequence<T> repeat(final T item) {
        return new Sequence<T>() {
            public final Iterator<T> iterator() {
                return Iterators.repeat(item);
            }
        };
    }

    public static Sequence<Number> range(final Number end) {
        return new Sequence<Number>() {
            public final Iterator<Number> iterator() {
                return Iterators.range(end);
            }
        };
    }

    public static Sequence<Number> range(final Number start, final Number end) {
        return new Sequence<Number>() {
            public final Iterator<Number> iterator() {
                return Iterators.range(start, end);
            }
        };
    }

    public static Sequence<Number> range(final Number start, final Number end, final Number step) {
        return new Sequence<Number>() {
            public final Iterator<Number> iterator() {
                return Iterators.range(start, end, step);
            }
        };
    }

    public static <T> void forEach(final Iterable<T> iterable, final Runnable1<T> runnable) {
        Iterators.forEach(iterable.iterator(), runnable);
    }

    public static <T> T first(final Iterable<T> iterable) {
        return head(iterable);
    }

    public static <T> T last(Iterable<T> iterable) {
        return head(reverse(iterable));
    }

    public static <T> T second(final Iterable<T> iterable) {
        return tail(iterable).head();
    }

    public static <T> T head(final Iterable<T> iterable) {
        return Iterators.head(iterable.iterator());
    }

    public static <T> Option<T> headOption(final Iterable<T> iterable) {
        return Iterators.headOption(iterable.iterator());
    }

    public static <T> Sequence<T> tail(final Iterable<T> iterable) {
        return new Sequence<T>() {
            public final Iterator<T> iterator() {
                return Iterators.tail(iterable.iterator());
            }
        };
    }

    public static <T, S> S fold(final Iterable<T> iterable, S seed, final Callable2<? super S, ? super T, S> callable) {
        return Iterators.fold(iterable.iterator(), seed, callable);
    }

    public static <T, S> S foldLeft(final Iterable<T> iterable, S seed, final Callable2<? super S, ? super T, S> callable) {
        return Iterators.foldLeft(iterable.iterator(), seed, callable);
    }

    public static <T> T reduce(final Iterable<T> iterable, final Callable2<? super T, ? super T, T> callable) {
        return Iterators.reduce(iterable.iterator(), callable);
    }

    public static <T> T reduceLeft(final Iterable<T> iterable, final Callable2<? super T, ? super T, T> callable) {
        return Iterators.reduceLeft(iterable.iterator(), callable);
    }

    public static String toString(final Iterable iterable) {
        return Iterators.toString(iterable.iterator());
    }

    public static String toString(final Iterable iterable, final String separator) {
        return Iterators.toString(iterable.iterator(), separator);
    }

    public static String toString(final Iterable iterable, final String start, final String separator, final String end) {
        return Iterators.toString(iterable.iterator(), start, separator, end);
    }

    public static String toString(final Iterable iterable, final String start, final String separator, final String end, final Number limit) {
        return Iterators.toString(iterable.iterator(), start, separator, end, limit);
    }

    public static boolean isEmpty(final Iterable iterable) {
        return !iterable.iterator().hasNext();
    }

    public static <T> List<T> toList(final Iterable<T> iterable) {
        return Iterators.toList(iterable.iterator());
    }

    public static <T> Sequence<T> remove(final Iterable<T> iterable, final T t) {
        return new Sequence<T>() {
            public final Iterator<T> iterator() {
                return Iterators.remove(iterable.iterator(), t);
            }
        };
    }

    public static <T> Number size(final Iterable<T> iterable) {
        return Iterators.size(iterable.iterator());
    }

    public static <T> Sequence<T> take(final Iterable<T> iterable, final int count) {
        return new Sequence<T>() {
            public final Iterator<T> iterator() {
                return Iterators.take(iterable.iterator(), count);
            }
        };
    }

    public static <T> Sequence<T> takeWhile(final Iterable<T> iterable, final Predicate<? super T> predicate) {
        return new Sequence<T>() {
            public final Iterator<T> iterator() {
                return Iterators.takeWhile(iterable.iterator(), predicate);
            }
        };
    }

    public static <T> Sequence<T> drop(final Iterable<T> iterable, final int count) {
        return new Sequence<T>() {
            public final Iterator<T> iterator() {
                return Iterators.drop(iterable.iterator(), count);
            }
        };
    }

    public static <T> Sequence<T> dropWhile(final Iterable<T> iterable, final Predicate<? super T> predicate) {
        return new Sequence<T>() {
            public final Iterator<T> iterator() {
                return Iterators.dropWhile(iterable.iterator(), predicate);
            }
        };
    }

    public static <T> boolean forAll(final Iterable<T> iterable, final Predicate<? super T> predicate) {
        return Iterators.forAll(iterable.iterator(), predicate);
    }

    public static <T> boolean contains(final Iterable<T> iterable, final T t) {
        return Iterators.contains(iterable.iterator(), t);
    }

    public static <T> boolean exists(final Iterable<T> iterable, final Predicate<? super T> predicate) {
        return Iterators.exists(iterable.iterator(), predicate);
    }

    public static <T> Option<T> find(final Iterable<T> iterable, final Predicate<? super T> predicate) {
        return Iterators.find(iterable.iterator(), predicate);
    }

    public static <T, S> Option<S> tryPick(final Iterable<T> iterable, final Callable1<T, Option<S>> callable) {
        return Iterators.tryPick(iterable.iterator(), callable);
    }

    public static <T, S> S pick(final Iterable<T> iterable, final Callable1<T, Option<S>> callable) {
        return Iterators.pick(iterable.iterator(), callable);
    }

    public static <T> Sequence<T> add(final Iterable<T> iterable, final T t) {
        return new Sequence<T>() {
            public final Iterator<T> iterator() {
                return Iterators.add(iterable.iterator(), t);
            }
        };
    }

    public static <T> Sequence<T> join(final Iterable<? extends T>... iterables) {
        return new Sequence<T>() {
            public final Iterator<T> iterator() {
                return Iterators.join(sequence(iterables).map(Callables.<T>asIterator()));
            }
        };
    }

    public static <T> Sequence<T> cons(final T t, final Iterable<T> iterable) {
        return new Sequence<T>() {
            public final Iterator<T> iterator() {
                return Iterators.cons(t, iterable.iterator());
            }
        };
    }

    public static <T> MemorisedSequence<T> memorise(final Iterable<T> iterable) {
        return new MemorisedSequence<T>(iterable);
    }

    public static <T1, T2> Sequence<Pair<T1, T2>> zip(final Iterable<T1> left, final Iterable<T2> right) {
        return new Sequence<Pair<T1, T2>>() {
            public final Iterator<Pair<T1, T2>> iterator() {
                return new ZipIterator<T1, T2>(left.iterator(), right.iterator());
            }
        };
    }

    public static <T> Sequence<Pair<Number, T>> zipWithIndex(final Iterable<T> iterable) {
        return zip(iterate(increment(), 0), iterable);
    }

    public static <T> Sequence<T> sortBy(final Iterable<T> iterable, final Callable1<? super T, ? extends Comparable> callable) {
        return sortBy(iterable, ascending(callable));
    }

    public static <T> Sequence<T> sortBy(final Iterable<T> iterable, final Comparator<? super T> comparator) {
        List<T> result = sequence(iterable).toList();
        Collections.sort(result, comparator);
        return sequence(result);
    }

    public static <T extends Comparable<? super T>> Sequence<T> sort(final Iterable<T> iterable) {
        List<T> result = sequence(iterable).toList();
        Collections.sort(result);
        return sequence(result);
    }

    public static <T, S> Sequence<S> safeCast(final Iterable<T> iterable, final Class<S> aClass) {
        return new Sequence<S>() {
            public final Iterator<S> iterator() {
                return Iterators.safeCast(iterable.iterator(), aClass);
            }
        };
    }

    public static <T> Sequence<T> realise(final Iterable<T> iterable) {
        return sequence(Iterators.toList(iterable.iterator()));
    }

    public static <T> Sequence<T> reverse(final Iterable<T> iterable) {
        List<T> result = sequence(iterable).toList();
        Collections.reverse(result);
        return sequence(result);
    }

}
