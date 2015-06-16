package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Function;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Predicate;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

import static com.googlecode.totallylazy.Callers.call;

public interface TreeMap<K, V> extends PersistentSortedMap<K, V> {
    Comparator<K> comparator();

    K key();

    V value();

    TreeMap<K, V> left();

    TreeMap<K, V> left(TreeMap<K, V> newLeft);

    TreeMap<K, V> right();

    TreeMap<K, V> right(TreeMap<K, V> newRight);

    TreeMap<K, V> rotateLeft();

    TreeMap<K, V> rotateRight();

    @Override
    TreeMap<K, V> empty();

    @Override
    TreeMap<K, V> cons(Pair<K, V> head);

    @Override
    TreeMap<K, V> tail() throws NoSuchElementException;

    @Override
    TreeMap<K, V> insert(K key, V value);

    @Override
    TreeMap<K, V> delete(K key);

    @Override
    TreeMap<K, V> filter(Predicate<? super Pair<K, V>> predicate);

    @Override
    TreeMap<K, V> filterKeys(Predicate<? super K> predicate);

    @Override
    TreeMap<K, V> filterValues(Predicate<? super V> predicate);

    @Override
    <NewV> TreeMap<K, NewV> map(Callable1<? super V, ? extends NewV> transformer);

    @Override
    Pair<? extends TreeMap<K, V>, Pair<K, V>> removeFirst();

    @Override
    Pair<? extends TreeMap<K, V>, Pair<K, V>> removeLast();

    TreeFactory factory();

    class methods {
        public static <K, V, NewV> TreeMap<K, NewV> map(Callable1<? super V, ? extends NewV> transformer, final TreeFactory factory, final TreeMap<K, V> treeMap) {
            return factory.create(treeMap.comparator(), treeMap.key(), call(transformer, treeMap.value()), treeMap.left().map(transformer), treeMap.right().map(transformer));
        }

        public static <K, V> TreeMap<K, V> treeMap(final TreeFactory factory, final Comparator<K> comparator, final List<Pair<K, V>> sortedList) {
            if(sortedList.size() == 0) return factory.create(comparator);
            int middle = sortedList.size() / 2;
            Pair<K, V> pair = sortedList.get(middle);
            TreeMap<K, V> left = treeMap(factory, comparator, sortedList.subList(0, middle));
            TreeMap<K, V> right = treeMap(factory, comparator, sortedList.subList(middle + 1, sortedList.size()));
            return factory.create(comparator, pair.first(), pair.second(), left, right);
        }
    }

    class functions {
        public static <K, V> Function<TreeMap<K, V>, TreeMap<K, V>> replace(final K key, final V value) {
            return new Function<TreeMap<K, V>, TreeMap<K, V>>() {
                @Override
                public TreeMap<K, V> call(TreeMap<K, V> focus) throws Exception {
                    return focus.factory().create(focus.comparator(), key, value, focus.left(), focus.right());
                }
            };
        }

        public static <K, V> Function<TreeMap<K, V>, TreeMap<K, V>> remove() {
            return new Function<TreeMap<K, V>, TreeMap<K, V>>() {
                @Override
                public TreeMap<K, V> call(TreeMap<K, V> focus) throws Exception {
                    return focus.delete(focus.key());
                }
            };
        }
    }
}