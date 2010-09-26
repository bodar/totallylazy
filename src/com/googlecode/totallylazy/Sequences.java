package com.googlecode.totallylazy;

import com.googlecode.totallylazy.iterators.ArrayIterator;
import com.googlecode.totallylazy.iterators.CharacterIterator;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static com.googlecode.totallylazy.Callables.increment;
import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.Predicates.not;
import static com.googlecode.totallylazy.Predicates.prime;
import static com.googlecode.totallylazy.Predicates.remainderIs;

public class Sequences {
    public static <T> Sequence<T> sequence(final Iterable<T> iterable) {
        return new Sequence<T>() {
            public Iterator<T> iterator() {
                return iterable.iterator();
            }
        };
    }

    public static <T> Sequence<T> sequence(final T... items) {
        return new Sequence<T>() {
            public Iterator<T> iterator() {
                return new ArrayIterator<T>(items);
            }
        };
    }

    public static Sequence<Character> characters(final String value) {
        return characters(value.toCharArray());
    }

    public static Sequence<Integer> primes() {
        return sequence(2).join(iterate(increment(), 3).filter(prime()));
    }

    private static Callable1<Pair<Integer, Sequence<Integer>>, Pair<Integer, Sequence<Integer>>> incrementFirst() {
        return new Callable1<Pair<Integer, Sequence<Integer>>, Pair<Integer, Sequence<Integer>>>() {
            public Pair<Integer, Sequence<Integer>> call(Pair<Integer, Sequence<Integer>> pair) throws Exception {
                return pair(pair.first() + 1, pair.second());
            }
        };
    }

    public static Sequence<Integer> fibonacci() {
        return iterate(addAndShift(), sequence(0, 1)).map(Callables.<Integer>first());
    }

    private static Callable1<Sequence<Integer>, Sequence<Integer>> addAndShift() {
        return new Callable1<Sequence<Integer>, Sequence<Integer>>() {
            public Sequence<Integer> call(Sequence<Integer> pairs) throws Exception {
                return sequence(pairs.second(), pairs.first() + pairs.second());
            }
        };
    }

    public static Sequence<Character> characters(final char[] value) {
        return new Sequence<Character>() {
            public Iterator<Character> iterator() {
                return new CharacterIterator(value);
            }
        };
    }

    public static <T, S> Sequence<S> map(final Iterable<T> iterable, final Callable1<? super T, S> callable) {
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

    public static <T, S> Sequence<S> flatMap(final Iterable<T> iterable, final Callable1<? super T, Iterable<S>> callable) {
        return new Sequence<S>() {
            public Iterator<S> iterator() {
                return Iterators.flatMap(iterable.iterator(), callable);
            }
        };
    }

    public static <T> Sequence<T> iterate(final Callable1<? super T, T> callable, final T t) {
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

    public static <T> T first(final Iterable<T> iterable) {
        return head(iterable);
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
            public Iterator<T> iterator() {
                return Iterators.tail(iterable.iterator());
            }
        };
    }

    public static <T, S> S foldLeft(final Iterable<T> iterable, S seed, Callable2<S, T, S> callable) {
        return Iterators.foldLeft(iterable.iterator(), seed, callable);
    }

    public static <T> T reduceLeft(final Iterable<T> iterable, Callable2<T, T, T> callable) {
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

    public static <T> boolean forAll(final Iterable<T> iterable, final Predicate<T> predicate) {
        return Iterators.forAll(iterable.iterator(), predicate);
    }

    public static <T> boolean contains(final Iterable<T> iterable, final T t) {
        return Iterators.contains(iterable.iterator(), t);
    }

    public static <T> boolean exists(final Iterable<T> iterable, final Predicate<T> predicate) {
        return Iterators.exists(iterable.iterator(), predicate);
    }

    public static <T> Option<T> find(final Iterable<T> iterable, final Predicate<T> predicate) {
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
            public Iterator<T> iterator() {
                return Iterators.add(iterable.iterator(), t);
            }
        };
    }

    public static <T> Sequence<T> join(final Iterable<T>... iterables) {
        return new Sequence<T>() {
            public Iterator<T> iterator() {
                return Iterators.join(sequence(iterables).map(Callables.<T>asIterator()));
            }
        };
    }
}
