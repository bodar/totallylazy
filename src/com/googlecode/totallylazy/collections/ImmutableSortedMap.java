package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callables;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.comparators.Comparators;

import java.util.Comparator;
import java.util.NoSuchElementException;

import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.Sequences.sequence;

public interface ImmutableSortedMap<K, V> extends ImmutableMap<K, V>, Sorted<Pair<K, V>>, Indexed<Pair<K, V>> {
    @Override
    ImmutableSortedMap<K, V> cons(Pair<K, V> head);

    @Override
    ImmutableSortedMap<K, V> tail() throws NoSuchElementException;

    @Override
    ImmutableSortedMap<K, V> put(K key, V value);

    @Override
    ImmutableSortedMap<K, V> remove(K key);

    @Override
    ImmutableSortedMap<K, V> filterKeys(Predicate<? super K> predicate);

    @Override
    ImmutableSortedMap<K, V> filterValues(Predicate<? super V> predicate);

    @Override
    <NewV> ImmutableSortedMap<K, NewV> map(Callable1<? super V, ? extends NewV> transformer);

    @Override
    Pair<? extends ImmutableSortedMap<K, V>, Pair<K, V>> removeFirst();

    @Override
    Pair<? extends ImmutableSortedMap<K, V>, Pair<K, V>> removeLast();

    class constructors {
        private static TreeFactory factory = AVLTree.constructors.factory;

        public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> emptySortedMap(Class<K> kClass, Class<V> vClass) {
            return constructors.<K, V>sortedMap();
        }

        public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> emptySortedMap() {
            return constructors.<K, V>sortedMap();
        }

        public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> sortedMap() {
            return constructors.<K, V>sortedMap(Comparators.<K>ascending());
        }

        public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> sortedMap(K key, V value) {
            return constructors.<K, V>sortedMap(Comparators.<K>ascending(), key, value);
        }

        public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> sortedMap(K key1, V value1, K key2, V value2) {
            return sortedMap(sequence(pair(key1, value1), pair(key2, value2)));
        }

        public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> sortedMap(K key1, V value1, K key2, V value2, K key3, V value3) {
            return sortedMap(sequence(pair(key1, value1), pair(key2, value2), pair(key3, value3)));
        }

        public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> sortedMap(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4) {
            return sortedMap(sequence(pair(key1, value1), pair(key2, value2), pair(key3, value3), pair(key4, value4)));
        }

        public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> sortedMap(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4, K key5, V value5) {
            return sortedMap(sequence(pair(key1, value1), pair(key2, value2), pair(key3, value3), pair(key4, value4), pair(key5, value5)));
        }

        public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> sortedMap(final Pair<K, V> head, final Pair<K, V>... tail) {
            return sortedMap(sequence(tail).cons(head));
        }

        public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> sortedMap(final Iterable<Pair<K, V>> values) {
            return sortedMap(Comparators.<K>ascending(), values);
        }

        public static <K, V> ImmutableSortedMap<K, V> sortedMap(Comparator<K> comparator) {
            return factory.create(comparator);
        }

        public static <K, V> ImmutableSortedMap<K, V> sortedMap(Comparator<K> comparator, K key, V value) {
            return factory.create(comparator, key, value);
        }

        public static <K, V> ImmutableSortedMap<K, V> sortedMap(Comparator<K> comparator, K key1, V value1, K key2, V value2) {
            return sortedMap(comparator, sequence(pair(key1, value1), pair(key2, value2)));
        }

        public static <K, V> ImmutableSortedMap<K, V> sortedMap(Comparator<K> comparator, K key1, V value1, K key2, V value2, K key3, V value3) {
            return sortedMap(comparator, sequence(pair(key1, value1), pair(key2, value2), pair(key3, value3)));
        }

        public static <K, V> ImmutableSortedMap<K, V> sortedMap(Comparator<K> comparator, K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4) {
            return sortedMap(comparator, sequence(pair(key1, value1), pair(key2, value2), pair(key3, value3), pair(key4, value4)));
        }

        public static <K, V> ImmutableSortedMap<K, V> sortedMap(Comparator<K> comparator, K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4, K key5, V value5) {
            return sortedMap(comparator, sequence(pair(key1, value1), pair(key2, value2), pair(key3, value3), pair(key4, value4), pair(key5, value5)));
        }

        public static <K, V> ImmutableSortedMap<K, V> sortedMap(Comparator<K> comparator, final Pair<K, V> head, final Pair<K, V>... tail) {
            return sortedMap(comparator, sequence(tail).cons(head));
        }

        public static <K, V> ImmutableSortedMap<K, V> sortedMap(Comparator<K> comparator, final Iterable<Pair<K, V>> values) {
            return TreeMap.methods.treeMap(factory, comparator, sequence(values).toSortedList(Comparators.<Pair<K, V>, K>where(Callables.<K>first(), comparator)));
        }
    }
}
