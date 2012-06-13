package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Predicate;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

import static com.googlecode.totallylazy.Callers.call;

public interface TreeMap<K, V> extends ImmutableSortedMap<K, V> {
    K key();

    V value();

    TreeMap<K, V> left();

    TreeMap<K, V> left(TreeMap<K, V> newLeft);

    TreeMap<K, V> right();

    TreeMap<K, V> right(TreeMap<K, V> newRight);

    @Override
    TreeMap<K, V> cons(Pair<K, V> head);

    @Override
    TreeMap<K, V> tail() throws NoSuchElementException;

    @Override
    TreeMap<K, V> put(K key, V value);

    @Override
    TreeMap<K, V> remove(K key);

    @Override
    TreeMap<K, V> filterKeys(Predicate<? super K> predicate);

    @Override
    TreeMap<K, V> filterValues(Predicate<? super V> predicate);

    @Override
    <NewV> TreeMap<K, NewV> mapValues(Callable1<? super V, ? extends NewV> transformer);

    @Override
    Pair<? extends TreeMap<K, V>, Pair<K, V>> removeFirst();

    @Override
    Pair<? extends TreeMap<K, V>, Pair<K, V>> removeLast();

    Comparator<K> comparator();

    enum constructors implements TreeFactory {
        factory;

        @Override
        public <K, V> TreeMap<K, V> create(Comparator<K> comparator) {
            return new AbstractEmptyTreeMap<K, V, TreeMap<K, V>>(comparator, this) {
            };
        }

        @Override
        public <K, V> TreeMap<K, V> create(Comparator<K> comparator, K key, V value) {
            return create(comparator, this.<K, V>create(comparator), key, value, this.<K, V>create(comparator));
        }

        @Override
        public <K, V> TreeMap<K, V> create(Comparator<K> comparator, TreeMap<K, V> left, K key, V value, TreeMap<K, V> right) {
            return new AbstractTreeMap<K, V, TreeMap<K, V>>(left, key, value, right, comparator, constructors.factory) {
            };
        }
    }

    class methods {
        public static <K, V, NewV> TreeMap<K, NewV> mapValues(Callable1<? super V, ? extends NewV> transformer, final TreeFactory factory, final TreeMap<K, V> treeMap) {
            return factory.create(treeMap.comparator(), treeMap.left().mapValues(transformer), treeMap.key(), call(transformer, treeMap.value()), treeMap.right().mapValues(transformer));
        }

        public static <K, V> TreeMap<K, V> treeMap(final TreeFactory factory, final Comparator<K> comparator, final List<Pair<K, V>> sortedList) {
            if(sortedList.size() == 0) return factory.create(comparator);
            int middle = sortedList.size() / 2;
            Pair<K, V> pair = sortedList.get(middle);
            TreeMap<K, V> left = treeMap(factory, comparator, sortedList.subList(0, middle));
            TreeMap<K, V> right = treeMap(factory, comparator, sortedList.subList(middle + 1, sortedList.size()));
            return factory.create(comparator, left, pair.first(), pair.second(), right);
        }
    }
}