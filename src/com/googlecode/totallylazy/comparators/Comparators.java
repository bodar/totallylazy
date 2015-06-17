package com.googlecode.totallylazy.comparators;

import com.googlecode.totallylazy.Function1;
import com.googlecode.totallylazy.Callables;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;

import java.util.Comparator;

import static com.googlecode.totallylazy.Callers.call;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Unchecked.cast;

public class Comparators {
    public static <T, R> Comparator<T> by(final Function1<? super T, ? extends R> callable, final Comparator<? super R> comparator) {
        return (instance, otherInstance) -> comparator.compare(call(callable, instance), call(callable, otherInstance));
    }

    public static <T, R> Comparator<T> where(final Function1<? super T, ? extends R> callable, final Comparator<? super R> comparator) {
        return by(callable, comparator);
    }

    public static <A, B> Comparator<Pair<A, B>> first(final Comparator<A> comparator) {
        return by(Callables.<A>first(), comparator);
    }

    public static <A, B> Comparator<Pair<A, B>> second(final Comparator<B> comparator) {
        return by(Callables.<B>second(), comparator);
    }

    @SuppressWarnings("unchecked")
    private static final Comparator<Comparable> ASCENDING = (a, b) -> a.compareTo(b);

    public static <T extends Comparable<? super T>> Comparator<T> ascending() {
        return cast(ASCENDING);
    }

    public static <T extends Comparable<? super T>> Comparator<T> ascending(Class<T> aClass) {
        return Comparators.<T>ascending();
    }

    public static <T, R extends Comparable<? super R>> Comparator<T> ascending(final Function1<? super T, ? extends R> callable) {
        return new AscendingComparator<T, R>(callable);
    }

    @SuppressWarnings("unchecked")
    private static final Comparator<Comparable> DESCENDING = (a, b) -> b.compareTo(a);

    public static <T extends Comparable<? super T>> Comparator<T> descending() {
        return cast(DESCENDING);
    }

    public static <T extends Comparable<? super T>> Comparator<T> descending(Class<T> aClass) {
        return Comparators.<T>descending();
    }

    public static <T, R extends Comparable<? super R>> Comparator<T> descending(final Function1<? super T, ? extends R> callable) {
        return new DescendingComparator<T, R>(callable);
    }

    @SafeVarargs
    public static <T> Comparator<T> comparators(final Comparator<? super T>... comparators) {
        return comparators(sequence(comparators));
    }

    public static <T> Comparator<T> comparators(final Sequence<Comparator<? super T>> comparators) {
        return new CompositeComparator<T>(comparators);
    }

    public static <T> Comparator<T> comparators(final Comparator<? super T>  first) {
        return comparators(Sequences.<Comparator<? super T>>sequence(first));
    }

    public static <T> Comparator<T> comparators(final Comparator<? super T>  first, final Comparator<? super T>  second) {
        return comparators(Sequences.<Comparator<? super T>>sequence(first, second));
    }

    public static <T> Comparator<T> comparators(final Comparator<? super T>  first, final Comparator<? super T>  second, final Comparator<? super T> third) {
        return comparators(Sequences.<Comparator<? super T>>sequence(first, second, third));
    }

    public static <T> Comparator<T> comparators(final Comparator<? super T>  first, final Comparator<? super T>  second, final Comparator<? super T> third, final Comparator<? super T> fourth) {
        return comparators(Sequences.<Comparator<? super T>>sequence(first, second, third, fourth));
    }

    public static <T> Comparator<T> comparators(final Comparator<? super T>  first, final Comparator<? super T>  second, final Comparator<? super T> third, final Comparator<? super T> fourth, final Comparator<? super T>  fifth) {
        return comparators(Sequences.<Comparator<? super T>>sequence(first, second, third, fourth, fifth));
    }

}
