package com.googlecode.totallylazy;

import com.googlecode.totallylazy.iterators.ArrayIterator;
import com.googlecode.totallylazy.iterators.CharacterIterator;
import com.googlecode.totallylazy.iterators.ZipIterator;

import java.util.*;

import static com.googlecode.totallylazy.Callables.*;
import static com.googlecode.totallylazy.Predicates.*;
import static java.nio.CharBuffer.wrap;

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

    public static <T> Sequence<T> sequence(final Iterator<T> iterator) {
        return new Sequence<T>() {
            public Iterator<T> iterator() {
                return iterator;
            }
        };
    }

    public static <T> Sequence<T> memorise(final Iterator<T> iterator) {
        return new MemorisedSequence<T>(new Iterable<T>() {
            public Iterator<T> iterator() {
                return iterator;
            }
        });
    }

    public static Sequence<Number> primeFactorsOf(Number value) {
        return primes().takeWhile(primeSquaredLessThan(value)).filter(remainderIsZero(value));
    }

//    public static Sequence<Number> primeFactorsOf(Number value) {
//        return iterate(incrementCandidateFactorAndReduceCeiling(), pair((Number)2, value)).filter(factorsOfCeiling()).takeWhile(factorIsLessThanCeiling()).map(Callables.<Number>first());
//    }
//
//    private static Predicate<? super Pair<Number, Number>> factorsOfCeiling() {
//        return new Predicate<Pair<Number, Number>>() {
//            public boolean matches(Pair<Number, Number> pair) {
//                return isZero(remainder(pair.second(), pair.first()));
//            }
//        };
//    }
//
//    private static Predicate<? super Pair<Number, Number>> factorIsLessThanCeiling() {
//        return new Predicate<Pair<Number, Number>>() {
//            public boolean matches(Pair<Number, Number> pair) {
//                return Numbers.lte(Numbers.multiply(pair.first(), pair.first()) , pair.second());
//            }
//        };
//    }
//
//    private static Callable1<? super Pair<Number, Number>, Pair<Number, Number>> incrementCandidateFactorAndReduceCeiling() {
//        return new Callable1<Pair<Number, Number>, Pair<Number, Number>>() {
//            public Pair<Number, Number> call(Pair<Number, Number> pair) throws Exception {
//                Number ceiling = pair.second();
//                if(isZero(remainder(pair.second(), pair.first()))){
//                    ceiling = Numbers.divide(pair.second(), pair.first());
//                }
//                return pair(Numbers.add(pair.first(), 1), ceiling);
//            }
//        };
//    }

    private static final MemorisedSequence<Number> primes = Sequences.<Number>sequence(2).join(iterate(Callables.add(2), 3).filter(prime())).memorise();

    public static MemorisedSequence<Number> primes() {
        return primes;
    }

    public static Sequence<Number> fibonacci() {
        return iterate(reduceAndShift(Callables.add()), Sequences.<Number>sequence(0, 1)).map(Callables.<Number>first());
    }

    public static Sequence<Number> powersOf(Number amount) {
        return iterate(multiply(amount), 1);
    }

    public static Sequence<Character> characters(final CharSequence value) {
        return new Sequence<Character>() {
            public Iterator<Character> iterator() {
                return new CharacterIterator(value);
            }
        };
    }

    public static Sequence<Character> characters(final char[] value) {
        return characters(wrap(value));
    }

    public static <T, S> Sequence<S> map(final Iterable<T> iterable, final Callable1<? super T, S> callable) {
        return new Sequence<S>() {
            public Iterator<S> iterator() {
                return Iterators.map(iterable.iterator(), callable);
            }
        };
    }

    public static <T> Sequence<T> filter(final Iterable<T> iterable, final Predicate<? super T> predicate) {
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

    public static <T> void forEach(final Iterable<T> iterable, final Runnable1<T> runnable) {
        Iterators.forEach(iterable.iterator(), runnable);
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

    public static <T, S> S foldLeft(final Iterable<T> iterable, S seed, Callable2<? super S, ? super T, S> callable) {
        return Iterators.foldLeft(iterable.iterator(), seed, callable);
    }

    public static <T> T reduceLeft(final Iterable<T> iterable, Callable2<? super T, ? super T, T> callable) {
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

    public static String toString(final Iterable iterable, String start, String separator, String end, int limit) {
        return Iterators.toString(iterable.iterator(), start, separator, end, limit);
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

    public static <T> Sequence<T> takeWhile(final Iterable<T> iterable, final Predicate<? super T> predicate) {
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

    public static <T> Sequence<T> dropWhile(final Iterable<T> iterable, final Predicate<? super T> predicate) {
        return new Sequence<T>() {
            public Iterator<T> iterator() {
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

    public static <T> Sequence<T> cons(final T t, final Iterable<T> iterable) {
        return new Sequence<T>() {
            public Iterator<T> iterator() {
                return Iterators.cons(t, iterable.iterator());
            }
        };
    }

    public static <T> MemorisedSequence<T> memorise(final Iterable<T> iterable) {
        return new MemorisedSequence<T>(iterable);
    }

    public static <T1, T2> Sequence<Pair<T1, T2>> zip(final Iterable<T1> left, final Iterable<T2> right) {
        return new Sequence<Pair<T1, T2>>() {
            public Iterator<Pair<T1, T2>> iterator() {
                return new ZipIterator<T1, T2>(left.iterator(), right.iterator());
            }
        };
    }

    public static <T> Sequence<Pair<Number, T>> zipWithIndex(final Iterable<T> iterable) {
        return zip(iterate(increment(), 0), iterable);
    }

    public static <T> Sequence<T> sortBy(final Iterable<T> iterable, final Callable1<T, ? extends Comparable> callable) {
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
            public Iterator<S> iterator() {
                return Iterators.safeCast(iterable.iterator(), aClass);
            }
        };
    }

    public static <T> Sequence<T> realise(final Iterable<T> iterable) {
        return sequence(Iterators.toList(iterable.iterator()));
    }
}
