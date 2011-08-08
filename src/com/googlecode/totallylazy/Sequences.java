package com.googlecode.totallylazy;

import com.googlecode.totallylazy.comparators.Comparators;
import com.googlecode.totallylazy.iterators.ArrayIterator;
import com.googlecode.totallylazy.iterators.CharacterIterator;
import com.googlecode.totallylazy.iterators.EmptyIterator;
import com.googlecode.totallylazy.iterators.EnumerationIterator;
import com.googlecode.totallylazy.iterators.PairIterator;
import com.googlecode.totallylazy.iterators.QuadrupleIterator;
import com.googlecode.totallylazy.iterators.TripleIterator;

import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;

import static com.googlecode.totallylazy.Callables.ascending;
import static com.googlecode.totallylazy.Callables.bounce;
import static com.googlecode.totallylazy.Callables.flip;
import static com.googlecode.totallylazy.Callers.callConcurrently;
import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.Predicates.not;
import static com.googlecode.totallylazy.Predicates.whileTrue;
import static com.googlecode.totallylazy.Triple.triple;
import static com.googlecode.totallylazy.numbers.Numbers.integersStartingFrom;
import static java.nio.CharBuffer.wrap;

public class Sequences {
    public static <T> Sequence<T> empty(Class<T> aClass) {
        return empty();
    }

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
        if (items == null) {
            return empty();
        }
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

    public static <T> ForwardOnlySequence<T> forwardOnly(final Iterator<T> iterator) {
        return new ForwardOnlySequence<T>(iterator);
    }

    public static <T> ForwardOnlySequence<T> forwardOnly(final Iterable<T> iterable) {
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

    public static <T> Pair<Sequence<T>, Sequence<T>> partition(final Iterable<T> iterable, final Predicate<? super T> predicate) {
        return Iterators.partition(iterable.iterator(), predicate);
    }

    public static <T> Sequence<T> filter(final Iterable<T> iterable, final Predicate<? super T> predicate) {
        return new Sequence<T>() {
            public final Iterator<T> iterator() {
                return Iterators.filter(iterable.iterator(), predicate);
            }
        };
    }

    public static <T, S> Sequence<S> flatMap(final Iterable<T> iterable, final Callable1<? super T, ? extends Iterable<S>> callable) {
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

    public static <T> void forEach(final Iterable<T> iterable, final Callable1<T, Void> runnable) {
        Iterators.forEach(iterable.iterator(), runnable);
    }

    public static <T> T first(final Iterable<T> iterable) {
        return head(iterable);
    }

    public static <T> T last(final Iterable<T> iterable) {
        return head(reverse(iterable));
    }

    public static <T> Option<T>  lastOption(final Iterable<T> iterable) {
        return headOption(reverse(iterable));
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

    public static <T, S> S foldRight(final Iterable<T> iterable, S seed, final Callable2<? super T, ? super S, S> callable) {
        return foldLeft(sequence(iterable).reverse(), seed, flip(callable));
    }

    public static <T, S> S reduce(final Iterable<T> iterable, final Callable2<? super S, ? super T, S> callable) {
        return Iterators.reduce(iterable.iterator(), callable);
    }

    public static <T, S> S reduceLeft(final Iterable<T> iterable, final Callable2<? super S, ? super T, S> callable) {
        return Iterators.reduceLeft(iterable.iterator(), callable);
    }

    public static <T, S> S reduceRight(final Iterable<T> iterable, final Callable2<? super T, ? super S, S> callable) {
        Iterator<T> iterator = iterable.iterator();
        return foldRight(forwardOnly(iterator), (S) iterator.next(), callable);
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
                return Iterators.join(sequence(iterables).map(new Callable1<Iterable<? extends T>, Iterator<T>>() {
                    public Iterator<T> call(Iterable<? extends T> iterable) throws Exception {
                        return (Iterator<T>) iterable.iterator();
                    }
                }));
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

    public static <F, S> Sequence<Pair<F, S>> zip(final Iterable<F> first, final Iterable<S> second) {
        return new Sequence<Pair<F, S>>() {
            public final Iterator<Pair<F, S>> iterator() {
                return new PairIterator<F, S>(first.iterator(), second.iterator());
            }
        };
    }

    public static <F, S, T> Sequence<Triple<F, S, T>> zip(final Iterable<F> first, final Iterable<S> second, final Iterable<T> third) {
        return new Sequence<Triple<F, S, T>>() {
            public final Iterator<Triple<F, S, T>> iterator() {
                return new TripleIterator<F, S, T>(first.iterator(), second.iterator(), third.iterator());
            }
        };
    }

    public static <F, S, T, Fo> Sequence<Quadruple<F, S, T, Fo>> zip(final Iterable<F> first, final Iterable<S> second, final Iterable<T> third, final Iterable<Fo> forth) {
        return new Sequence<Quadruple<F, S, T, Fo>>() {
            public final Iterator<Quadruple<F, S, T, Fo>> iterator() {
                return new QuadrupleIterator<F, S, T, Fo>(first.iterator(), second.iterator(), third.iterator(), forth.iterator());
            }
        };
    }

    public static <F, S> Pair<Sequence<F>, Sequence<S>> unzip(final Iterable<Pair<F, S>> pairs) {
        return pair(sequence(pairs).map(Callables.<F>first()),
                sequence(pairs).map(Callables.<S>second()));
    }

    public static <F, S, T> Triple<Sequence<F>, Sequence<S>, Sequence<T>> unzip3(final Iterable<Triple<F, S, T>> triples) {
        return triple(sequence(triples).map(Callables.<F>first()),
                sequence(triples).map(Callables.<S>second()),
                sequence(triples).map(Callables.<T>third()));
    }

    public static <F, S, T, Fo> Quadruple<Sequence<F>, Sequence<S>, Sequence<T>, Sequence<Fo>> unzip4(final Iterable<Quadruple<F, S, T, Fo>> quadruples) {
        return Quadruple.quadruple(sequence(quadruples).map(Callables.<F>first()),
                sequence(quadruples).map(Callables.<S>second()),
                sequence(quadruples).map(Callables.<T>third()),
                sequence(quadruples).map(Callables.<Fo>fourth()));
    }


    public static <T> Sequence<Pair<Number, T>> zipWithIndex(final Iterable<T> iterable) {
        return zip(integersStartingFrom(0), iterable);
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
        return sort(iterable, Comparators.<T>ascending());
    }

    public static <T extends Comparable<? super T>> Sequence<T> sort(final Iterable<T> iterable, Comparator<? super T> comparator) {
        List<T> result = sequence(iterable).toList();
        Collections.sort(result, comparator);
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
        final List<T> result = sequence(iterable).toList();
        Collections.reverse(result);
        return sequence(result);
    }

    public static <T> Sequence<T> cycle(final Iterable<T> iterable) {
        return repeat(sequence(iterable).memorise()).flatMap(Callables.<Iterable<T>>returnArgument());
    }

    public static <T, S> Sequence<S> mapConcurrently(final Iterable<T> iterable, final Callable1<? super T, S> callable) {
        return callConcurrently(sequence(iterable).map(bounce(callable)));
    }

    public static <T, S> Sequence<S> mapConcurrently(final Iterable<T> iterable, final Callable1<? super T, S> callable, final Executor executor) {
        return callConcurrently(sequence(iterable).map(bounce(callable)), executor);
    }

    public static <T, Key> Sequence<Group<Key, T>> groupBy(final Iterable<T> iterable, final Callable1<? super T, Key> callable) {
        return sequence(Maps.toMap(iterable.iterator(), callable).entrySet()).map(new Callable1<Map.Entry<Key, List<T>>, Group<Key, T>>() {
            public Group<Key, T> call(Map.Entry<Key, List<T>> entry) throws Exception {
                return new Group<Key, T>(entry.getKey(), entry.getValue());
            }
        });
    }

    public static boolean equalTo(Iterable iterable, Iterable other) {
        return Iterators.equalsTo(iterable.iterator(), other.iterator());
    }

    public static <T> Pair<Sequence<T>, Sequence<T>> splitAt(final Iterable<T> iterable, final Number index) {
        return sequence(iterable).partition(Predicates.countTo(index));
    }

    public static <T> Pair<Sequence<T>, Sequence<T>> span(final Iterable<T> iterable, final Predicate<? super T> predicate) {
        return sequence(iterable).partition(whileTrue(predicate));
    }

    public static <T> Pair<Sequence<T>, Sequence<T>> breakOn(final Iterable<T> iterable, final Predicate<? super T> predicate) {
        return sequence(iterable).partition(whileTrue(Predicates.<T>not(predicate)));
    }

    public static <T> Callable1<Sequence<T>, Pair<Sequence<T>, Sequence<T>>> splitAt(final Number index) {
        return new Callable1<Sequence<T>, Pair<Sequence<T>, Sequence<T>>>() {
            public Pair<Sequence<T>, Sequence<T>> call(Sequence<T> sequence) throws Exception {
                return sequence.splitAt(index);
            }
        };
    }

    public static <T> Sequence<Sequence<T>> recursive(final Iterable<T> iterable,
                                                      final Callable1<Sequence<T>, Pair<Sequence<T>, Sequence<T>>> callable) {
        return iteratePair(callable, sequence(iterable)).takeWhile(not(Predicates.<T>empty()));
    }

    public static <F, S> Sequence<F> iteratePair(final Callable1<S, Pair<F, S>> callable, final S instance){
        return iterate(applyToSecond(callable), Callers.call(callable, instance)).
                map(Callables.<F>first());
    }

    public static <F, S> Callable1<? super Pair<F, S>, Pair<F, S>> applyToSecond(final Callable1<S, Pair<F, S>> callable) {
        return new Callable1<Pair<F, S>, Pair<F, S>>() {
            public Pair<F, S> call(Pair<F, S> pair) throws Exception {
                return callable.call(pair.second());
            }
        };
    }


}