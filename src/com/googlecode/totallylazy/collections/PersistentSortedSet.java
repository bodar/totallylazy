package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.comparators.Comparators;

import java.util.Comparator;
import java.util.NoSuchElementException;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.collections.TreeSet.treeSet;

public interface PersistentSortedSet<T> extends PersistentSet<T>, Sorted<T>, Indexed<T> {
    @Override
    PersistentSortedSet<T> cons(T head);

    @Override
    PersistentSortedSet<T> put(T value);

    @Override
    PersistentSortedSet<T> remove(T value);

    @Override
    PersistentSortedSet<T> filter(Predicate<? super T> predicate);

    @Override
    <NewT> PersistentSortedSet<NewT> map(Callable1<? super T, ? extends NewT> transformer);

    @Override
    Pair<PersistentSortedSet<T>, T> removeFirst() throws NoSuchElementException;

    @Override
    Pair<PersistentSortedSet<T>, T> removeLast() throws NoSuchElementException;

    class constructors {
        public static <A extends Comparable<? super A>> PersistentSortedSet<A> sortedSet() {
            return constructors.<A>sortedSet(Comparators.<A>ascending());
        }

        public static <A extends Comparable<? super A>> PersistentSortedSet<A> sortedSet(A value) {
            return sortedSet(Comparators.<A>ascending(), value);
        }

        public static <A extends Comparable<? super A>> PersistentSortedSet<A> sortedSet(A value1, A value2) {
            return sortedSet(sequence(value1, value2));
        }

        public static <A extends Comparable<? super A>> PersistentSortedSet<A> sortedSet(A value1, A value2, A value3) {
            return sortedSet(sequence(value1, value2, value3));
        }

        public static <A extends Comparable<? super A>> PersistentSortedSet<A> sortedSet(A value1, A value2, A value3, A value4) {
            return sortedSet(sequence(value1, value2, value3, value4));
        }

        public static <A extends Comparable<? super A>> PersistentSortedSet<A> sortedSet(A value1, A value2, A value3, A value4, A value5) {
            return sortedSet(sequence(value1, value2, value3, value4, value5));
        }

        public static <A extends Comparable<? super A>> PersistentSortedSet<A> sortedSet(final A head, final A... tail) {
            return sortedSet(sequence(tail).cons(head));
        }

        public static <A extends Comparable<? super A>> PersistentSortedSet<A> sortedSet(final Iterable<A> values) {
            return sortedSet(Comparators.<A>ascending(), values);
        }

        public static <A> PersistentSortedSet<A> sortedSet(Comparator<A> comparator) {
            return treeSet(PersistentSortedMap.constructors.<A, A>sortedMap(comparator));
        }

        public static <A> PersistentSortedSet<A> sortedSet(Comparator<A> comparator, A value) {
            return treeSet(PersistentSortedMap.constructors.<A, A>sortedMap(comparator, value, value));
        }

        public static <A> PersistentSortedSet<A> sortedSet(Comparator<A> comparator, A value1, A value2) {
            return sortedSet(comparator, sequence(value1, value2));
        }

        public static <A> PersistentSortedSet<A> sortedSet(Comparator<A> comparator, A value1, A value2, A value3) {
            return sortedSet(comparator, sequence(value1, value2, value3));
        }

        public static <A> PersistentSortedSet<A> sortedSet(Comparator<A> comparator, A value1, A value2, A value3, A value4) {
            return sortedSet(comparator, sequence(value1, value2, value3, value4));
        }

        public static <A> PersistentSortedSet<A> sortedSet(Comparator<A> comparator, A value1, A value2, A value3, A value4, A value5) {
            return sortedSet(comparator, sequence(value1, value2, value3, value4, value5));
        }

        public static <A> PersistentSortedSet<A> sortedSet(Comparator<A> comparator, final A head, final A... tail) {
            return sortedSet(comparator, sequence(tail).cons(head));
        }

        public static <A> PersistentSortedSet<A> sortedSet(Comparator<A> comparator, final Iterable<A> values) {
            return sequence(values).fold(constructors.<A>sortedSet(comparator), functions.<A, PersistentSortedSet<A>>cons());
        }
    }
}
