package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Container;
import com.googlecode.totallylazy.Segment;
import com.googlecode.totallylazy.comparators.Comparators;

import java.util.Comparator;

import static com.googlecode.totallylazy.Sequences.sequence;

public interface ImmutableSet<T> extends Iterable<T>, Segment<T, ImmutableSet<T>>, Container<T> {
    ImmutableList<T> immutableList();

    class constructors {
        public static <T extends Comparable<? super T>> ImmutableSet<T> set(T value) {
            return TreeSet.tree(value);
        }

        public static <T> ImmutableSet<T> set(T value, Comparator<T> comparator) {
            return TreeSet.tree(value, comparator);
        }

        public static <T extends Comparable<? super T>> ImmutableSet<T> set(final T... values) {
            return set(sequence(values));
        }

        public static <T> ImmutableSet<T> set(Comparator<T> comparator, final T... values) {
            return set(sequence(values), comparator);
        }

        public static <T extends Comparable<? super T>> ImmutableSet<T> set(final Iterable<T> values) {
            return set(values, Comparators.<T>ascending());
        }

        public static <T> ImmutableSet<T> set(final Iterable<T> values, Comparator<T> comparator) {
            return sequence(values).fold(EmptySet.<T>empty(comparator), functions.<T, ImmutableSet<T>>cons());
        }


    }
}
