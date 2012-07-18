package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Container;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Segment;
import com.googlecode.totallylazy.comparators.Comparators;

import java.util.Comparator;
import java.util.NoSuchElementException;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.collections.TreeSet.treeSet;

public interface ImmutableSortedSet<T> extends ImmutableSet<T>, Sorted<T>, Indexed<T> {
    @Override
    ImmutableSortedSet<T> cons(T head);

    @Override
    ImmutableSortedSet<T> put(T value);

    @Override
    ImmutableSortedSet<T> remove(T value);

    @Override
    ImmutableSortedSet<T> filter(Predicate<? super T> predicate);

    @Override
    <NewT> ImmutableSortedSet<NewT> map(Callable1<? super T, ? extends NewT> transformer);

    @Override
    Pair<ImmutableSortedSet<T>, T> removeFirst() throws NoSuchElementException;

    @Override
    Pair<ImmutableSortedSet<T>, T> removeLast() throws NoSuchElementException;

    class constructors {
        public static <A extends Comparable<? super A>> ImmutableSortedSet<A> sortedSet() {
            return constructors.<A>sortedSet(Comparators.<A>ascending());
        }

        public static <A extends Comparable<? super A>> ImmutableSortedSet<A> sortedSet(A value) {
            return sortedSet(Comparators.<A>ascending(), value);
        }

        public static <A extends Comparable<? super A>> ImmutableSortedSet<A> sortedSet(A value1, A value2) {
            return sortedSet(sequence(value1, value2));
        }

        public static <A extends Comparable<? super A>> ImmutableSortedSet<A> sortedSet(A value1, A value2, A value3) {
            return sortedSet(sequence(value1, value2, value3));
        }

        public static <A extends Comparable<? super A>> ImmutableSortedSet<A> sortedSet(A value1, A value2, A value3, A value4) {
            return sortedSet(sequence(value1, value2, value3, value4));
        }

        public static <A extends Comparable<? super A>> ImmutableSortedSet<A> sortedSet(A value1, A value2, A value3, A value4, A value5) {
            return sortedSet(sequence(value1, value2, value3, value4, value5));
        }

        public static <A extends Comparable<? super A>> ImmutableSortedSet<A> sortedSet(final A head, final A... tail) {
            return sortedSet(sequence(tail).cons(head));
        }

        public static <A extends Comparable<? super A>> ImmutableSortedSet<A> sortedSet(final Iterable<A> values) {
            return sortedSet(Comparators.<A>ascending(), values);
        }

        public static <A> ImmutableSortedSet<A> sortedSet(Comparator<A> comparator) {
            return treeSet(ImmutableSortedMap.constructors.<A, A>sortedMap(comparator));
        }

        public static <A> ImmutableSortedSet<A> sortedSet(Comparator<A> comparator, A value) {
            return treeSet(ImmutableSortedMap.constructors.<A, A>sortedMap(comparator, value, value));
        }

        public static <A> ImmutableSortedSet<A> sortedSet(Comparator<A> comparator, A value1, A value2) {
            return sortedSet(comparator, sequence(value1, value2));
        }

        public static <A> ImmutableSortedSet<A> sortedSet(Comparator<A> comparator, A value1, A value2, A value3) {
            return sortedSet(comparator, sequence(value1, value2, value3));
        }

        public static <A> ImmutableSortedSet<A> sortedSet(Comparator<A> comparator, A value1, A value2, A value3, A value4) {
            return sortedSet(comparator, sequence(value1, value2, value3, value4));
        }

        public static <A> ImmutableSortedSet<A> sortedSet(Comparator<A> comparator, A value1, A value2, A value3, A value4, A value5) {
            return sortedSet(comparator, sequence(value1, value2, value3, value4, value5));
        }

        public static <A> ImmutableSortedSet<A> sortedSet(Comparator<A> comparator, final A head, final A... tail) {
            return sortedSet(comparator, sequence(tail).cons(head));
        }

        public static <A> ImmutableSortedSet<A> sortedSet(Comparator<A> comparator, final Iterable<A> values) {
            return sequence(values).fold(constructors.<A>sortedSet(comparator), functions.<A, ImmutableSortedSet<A>>cons());
        }
    }
}
