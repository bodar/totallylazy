package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Container;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Segment;
import com.googlecode.totallylazy.comparators.Comparators;

import java.util.Comparator;

import static com.googlecode.totallylazy.Sequences.sequence;

public interface ImmutableSet<T> extends Iterable<T>, Segment<T, ImmutableSet<T>>, Container<T> {
    ImmutableList<T> immutableList();

    ImmutableSet<T> put(T value);

    Option<T> find(Predicate<? super T> predicate);

    ImmutableSet<T> filter(Predicate<? super T> predicate);

    <NewT> ImmutableSet<NewT> map(Callable1<? super T, ? extends NewT> transformer);

    ImmutableSet<T> remove(T value);

    Pair<ImmutableSet<T>, T> removeMinimum();

    Pair<ImmutableSet<T>, T> removeMaximum();


    class constructors {
        public static <A extends Comparable<? super A>> ImmutableSet<A> sortedSet() {
            return constructors.<A>sortedSet(Comparators.<A>ascending());
        }

        public static <A extends Comparable<? super A>> ImmutableSet<A> sortedSet(A value) {
            return TreeSet.tree(value);
        }

        public static <A extends Comparable<? super A>> ImmutableSet<A> sortedSet(A value1, A value2) {
            return sortedSet(sequence(value1, value2));
        }

        public static <A extends Comparable<? super A>> ImmutableSet<A> sortedSet(A value1, A value2, A value3) {
            return sortedSet(sequence(value1, value2, value3));
        }

        public static <A extends Comparable<? super A>> ImmutableSet<A> sortedSet(A value1, A value2, A value3, A value4) {
            return sortedSet(sequence(value1, value2, value3, value4));
        }

        public static <A extends Comparable<? super A>> ImmutableSet<A> sortedSet(A value1, A value2, A value3, A value4, A value5) {
            return sortedSet(sequence(value1, value2, value3, value4, value5));
        }

        public static <A extends Comparable<? super A>> ImmutableSet<A> sortedSet(final A head, final A... tail) {
            return sortedSet(sequence(tail).cons(head));
        }

        public static <A extends Comparable<? super A>> ImmutableSet<A> sortedSet(final Iterable<A> values) {
            return sortedSet(Comparators.<A>ascending(), values);
        }

        public static <A> ImmutableSet<A> sortedSet(Comparator<A> comparator) {
            return TreeSet.<A>empty(comparator);
        }

        public static <A> ImmutableSet<A> sortedSet(Comparator<A> comparator, A value) {
            return TreeSet.tree(value, comparator);
        }

        public static <A> ImmutableSet<A> sortedSet(Comparator<A> comparator, A value1, A value2) {
            return sortedSet(comparator, sequence(value1, value2));
        }

        public static <A> ImmutableSet<A> sortedSet(Comparator<A> comparator, A value1, A value2, A value3) {
            return sortedSet(comparator, sequence(value1, value2, value3));
        }

        public static <A> ImmutableSet<A> sortedSet(Comparator<A> comparator, A value1, A value2, A value3, A value4) {
            return sortedSet(comparator, sequence(value1, value2, value3, value4));
        }

        public static <A> ImmutableSet<A> sortedSet(Comparator<A> comparator, A value1, A value2, A value3, A value4, A value5) {
            return sortedSet(comparator, sequence(value1, value2, value3, value4, value5));
        }

        public static <A> ImmutableSet<A> sortedSet(Comparator<A> comparator, final A head, final A... tail) {
            return sortedSet(comparator, sequence(tail).cons(head));
        }

        public static <A> ImmutableSet<A> sortedSet(Comparator<A> comparator, final Iterable<A> values) {
            return sequence(values).fold(constructors.<A>sortedSet(comparator), functions.<A, ImmutableSet<A>>cons());
        }
    }
}
