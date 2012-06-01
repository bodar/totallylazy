package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.None;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Segment;
import com.googlecode.totallylazy.comparators.Comparators;
import com.googlecode.totallylazy.iterators.EmptyIterator;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class EmptyMap<K, V> implements ImmutableMap<K, V> {
    final Comparator<K> comparator;

    EmptyMap(Comparator<K> comparator) {
        this.comparator = comparator;
    }

    public static <K extends Comparable<? super K>, V> EmptyMap<K, V> empty(Class<K> keyClass, Class<V> valueClass) {
        return EmptyMap.<K, V>empty();
    }

    public static <K extends Comparable<? super K>, V> EmptyMap<K, V> empty() {
        return empty(Comparators.<K>ascending());
    }

    public static <K, V> EmptyMap<K, V> empty(Comparator<K> comparator) {
        return new EmptyMap<K, V>(comparator);
    }

    @Override
    public ImmutableList<Pair<K, V>> immutableList() {
        return ImmutableList.constructors.empty();
    }

    @Override
    public None<V> get(K key) {
        return None.none();
    }

    @Override
    public ImmutableMap<K, V> put(K key, V value) {
        return cons(Pair.pair(key, value));
    }

    @Override
    public None<V> find(Predicate<? super K> predicate) {
        return None.none();
    }

    @Override
    public ImmutableMap<K, V> filterKeys(Predicate<? super K> predicate) {
        return this;
    }

    @Override
    public <C extends Segment<Pair<K, V>, C>> C join(C rest) {
        return rest;
    }

    @Override
    public ImmutableMap<K, V> cons(Pair<K, V> newValue) {
        return constructors.map(comparator, newValue.first(), newValue.second());
    }

    @Override
    public boolean contains(K other) {
        return false;
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof EmptyMap;
    }

    @Override
    public String toString() {
        return "";
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public Pair<K, V> head() throws NoSuchElementException {
        throw new NoSuchElementException();
    }

    @Override
    public ImmutableMap<K, V> tail() throws NoSuchElementException {
        throw new NoSuchElementException();
    }

    @Override
    public Iterator<Pair<K, V>> iterator() {
        return new EmptyIterator<Pair<K, V>>();
    }
}
