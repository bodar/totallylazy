package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.comparators.Comparators;

import java.util.Comparator;

import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.Sequences.sequence;

public interface SortedImmutableMap<K, V> extends ImmutableMap<K, V> {
    @Override
    SortedImmutableMap<K, V> cons(Pair<K, V> head);

    @Override
    SortedImmutableMap<K, V> put(K key, V value);

    @Override
    SortedImmutableMap<K, V> remove(K key);

    @Override
    SortedImmutableMap<K, V> filterKeys(Predicate<? super K> predicate);

    @Override
    SortedImmutableMap<K, V> filterValues(Predicate<? super V> predicate);

    @Override
    <NewV> SortedImmutableMap<K, NewV> mapValues(Callable1<? super V, ? extends NewV> transformer);

    Pair<SortedImmutableMap<K, V>, Pair<K, V>> removeMinimum();

    Pair<SortedImmutableMap<K, V>, Pair<K, V>> removeMaximum();

    V index(int i);

    class constructors {
        public static <K extends Comparable<? super K>, V> SortedImmutableMap<K, V> emptySortedMap(Class<K> kClass, Class<V> vClass) {
            return constructors.<K, V>sortedMap();
        }

        public static <K extends Comparable<? super K>, V> SortedImmutableMap<K, V> emptySortedMap() {
            return constructors.<K, V>sortedMap();
        }

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
