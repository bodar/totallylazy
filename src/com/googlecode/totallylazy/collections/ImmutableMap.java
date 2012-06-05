package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callables;
import com.googlecode.totallylazy.Container;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Segment;
import com.googlecode.totallylazy.comparators.Comparators;

import java.util.Comparator;
import java.util.Map;

import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.Sequences.sequence;

public interface ImmutableMap<K, V> extends Iterable<Pair<K, V>>, Segment<Pair<K, V>, ImmutableMap<K, V>>, Container<K> {
    ImmutableList<Pair<K, V>> immutableList();

    Option<V> get(K key);

    ImmutableMap<K, V> put(K key, V value);

    Option<V> find(Predicate<? super K> predicate);

    ImmutableMap<K, V> filterKeys(Predicate<? super K> predicate);

    ImmutableMap<K, V> filterValues(Predicate<? super V> predicate);

    <NewV> ImmutableMap<K, NewV> mapValues(Callable1<? super V, ? extends NewV> transformer);

    class constructors {
        public static <K extends Comparable<? super K>, V> ImmutableMap<K, V> map(K key, V value) {
            return TreeMap.tree(key, value);
        }

        public static <K extends Comparable<? super K>, V> ImmutableMap<K, V> map(K key1, V value1, K key2, V value2) {
            return map(sequence(pair(key1, value1), pair(key2, value2)));
        }

        public static <K extends Comparable<? super K>, V> ImmutableMap<K, V> map(K key1, V value1, K key2, V value2, K key3, V value3) {
            return map(sequence(pair(key1, value1), pair(key2, value2), pair(key3, value3)));
        }

        public static <K extends Comparable<? super K>, V> ImmutableMap<K, V> map(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4) {
            return map(sequence(pair(key1, value1), pair(key2, value2), pair(key3, value3), pair(key4, value4)));
        }

        public static <K extends Comparable<? super K>, V> ImmutableMap<K, V> map(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4, K key5, V value5) {
            return map(sequence(pair(key1, value1), pair(key2, value2), pair(key3, value3), pair(key4, value4), pair(key5, value5)));
        }

        public static <K extends Comparable<? super K>, V> ImmutableMap<K, V> map(final Pair<K, V>... values) {
            return map(sequence(values));
        }

        public static <K extends Comparable<? super K>, V> ImmutableMap<K, V> map(final Iterable<Pair<K, V>> values) {
            return map(Comparators.<K>ascending(), values);
        }

        public static <K, V> ImmutableMap<K, V> map(Comparator<K> comparator, K key, V value) {
            return TreeMap.tree(key, value, comparator);
        }

        public static <K, V> ImmutableMap<K, V> map(Comparator<K> comparator, K key1, V value1, K key2, V value2) {
            return map(comparator, sequence(pair(key1, value1), pair(key2, value2)));
        }

        public static <K, V> ImmutableMap<K, V> map(Comparator<K> comparator, K key1, V value1, K key2, V value2, K key3, V value3) {
            return map(comparator, sequence(pair(key1, value1), pair(key2, value2), pair(key3, value3)));
        }

        public static <K, V> ImmutableMap<K, V> map(Comparator<K> comparator, K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4) {
            return map(comparator, sequence(pair(key1, value1), pair(key2, value2), pair(key3, value3), pair(key4, value4)));
        }

        public static <K, V> ImmutableMap<K, V> map(Comparator<K> comparator, K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4, K key5, V value5) {
            return map(comparator, sequence(pair(key1, value1), pair(key2, value2), pair(key3, value3), pair(key4, value4), pair(key5, value5)));
        }

        public static <K, V> ImmutableMap<K, V> map(Comparator<K> comparator, final Pair<K, V>... values) {
            return map(comparator, sequence(values));
        }

        public static <K, V> ImmutableMap<K, V> map(Comparator<K> comparator, final Iterable<Pair<K, V>> values) {
            return sequence(values).fold(constructors.<K, V>empty(comparator), functions.<Pair<K, V>, ImmutableMap<K, V>>cons());
        }

        public static <K extends Comparable<? super K>, V> ImmutableMap<K, V> empty() {
            return constructors.<K, V>empty(Comparators.<K>ascending());
        }

        public static <K, V> ImmutableMap<K, V> empty(Comparator<K> comparator) {
            return EmptyMap.<K, V>empty(comparator);
        }


    }
}
