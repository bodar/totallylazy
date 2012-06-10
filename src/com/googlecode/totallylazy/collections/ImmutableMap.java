package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Container;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Segment;
import com.googlecode.totallylazy.comparators.Comparators;

import java.util.Comparator;

import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.Sequences.sequence;

public interface ImmutableMap<K, V> extends Iterable<Pair<K, V>>, Segment<Pair<K, V>>, Container<K> {
    ImmutableList<Pair<K, V>> immutableList();

    Option<V> get(K key);

    ImmutableMap<K, V> put(K key, V value);

    Option<V> find(Predicate<? super K> predicate);

    ImmutableMap<K, V> filterKeys(Predicate<? super K> predicate);

    ImmutableMap<K, V> filterValues(Predicate<? super V> predicate);

    <NewV> ImmutableMap<K, NewV> mapValues(Callable1<? super V, ? extends NewV> transformer);

    ImmutableMap<K, V> remove(K key);

    int size();

    class constructors {
        public static <K extends Comparable<? super K>, V> SortedImmutableMap<K, V> sortedMap() {
            return constructors.<K, V>sortedMap(Comparators.<K>ascending());
        }

        public static <K extends Comparable<? super K>, V> SortedImmutableMap<K, V> sortedMap(K key, V value) {
            return constructors.<K, V>sortedMap(Comparators.<K>ascending(), key, value);
        }

        public static <K extends Comparable<? super K>, V> SortedImmutableMap<K, V> sortedMap(K key1, V value1, K key2, V value2) {
            return sortedMap(sequence(pair(key1, value1), pair(key2, value2)));
        }

        public static <K extends Comparable<? super K>, V> SortedImmutableMap<K, V> sortedMap(K key1, V value1, K key2, V value2, K key3, V value3) {
            return sortedMap(sequence(pair(key1, value1), pair(key2, value2), pair(key3, value3)));
        }

        public static <K extends Comparable<? super K>, V> SortedImmutableMap<K, V> sortedMap(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4) {
            return sortedMap(sequence(pair(key1, value1), pair(key2, value2), pair(key3, value3), pair(key4, value4)));
        }

        public static <K extends Comparable<? super K>, V> SortedImmutableMap<K, V> sortedMap(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4, K key5, V value5) {
            return sortedMap(sequence(pair(key1, value1), pair(key2, value2), pair(key3, value3), pair(key4, value4), pair(key5, value5)));
        }

        public static <K extends Comparable<? super K>, V> SortedImmutableMap<K, V> sortedMap(final Pair<K, V> head, final Pair<K, V>... tail) {
            return sortedMap(sequence(tail).cons(head));
        }

        public static <K extends Comparable<? super K>, V> SortedImmutableMap<K, V> sortedMap(final Iterable<Pair<K, V>> values) {
            return sortedMap(Comparators.<K>ascending(), values);
        }

        public static <K, V> SortedImmutableMap<K, V> sortedMap(Comparator<K> comparator) {
            return AVLTree.constructors.<K, V>empty(comparator);
        }

        public static <K, V> SortedImmutableMap<K, V> sortedMap(Comparator<K> comparator, K key, V value) {
            return AVLTree.constructors.node(comparator, key, value);
        }

        public static <K, V> SortedImmutableMap<K, V> sortedMap(Comparator<K> comparator, K key1, V value1, K key2, V value2) {
            return sortedMap(comparator, sequence(pair(key1, value1), pair(key2, value2)));
        }

        public static <K, V> SortedImmutableMap<K, V> sortedMap(Comparator<K> comparator, K key1, V value1, K key2, V value2, K key3, V value3) {
            return sortedMap(comparator, sequence(pair(key1, value1), pair(key2, value2), pair(key3, value3)));
        }

        public static <K, V> SortedImmutableMap<K, V> sortedMap(Comparator<K> comparator, K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4) {
            return sortedMap(comparator, sequence(pair(key1, value1), pair(key2, value2), pair(key3, value3), pair(key4, value4)));
        }

        public static <K, V> SortedImmutableMap<K, V> sortedMap(Comparator<K> comparator, K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4, K key5, V value5) {
            return sortedMap(comparator, sequence(pair(key1, value1), pair(key2, value2), pair(key3, value3), pair(key4, value4), pair(key5, value5)));
        }

        public static <K, V> SortedImmutableMap<K, V> sortedMap(Comparator<K> comparator, final Pair<K, V> head, final Pair<K, V>... tail) {
            return sortedMap(comparator, sequence(tail).cons(head));
        }

        public static <K, V> SortedImmutableMap<K, V> sortedMap(Comparator<K> comparator, final Iterable<Pair<K, V>> values) {
            return sequence(values).fold(constructors.<K, V>sortedMap(comparator), functions.<Pair<K, V>, SortedImmutableMap<K, V>>cons());
        }
    }
}
