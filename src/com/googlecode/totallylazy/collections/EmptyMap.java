package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callable2;
import com.googlecode.totallylazy.Callers;
import com.googlecode.totallylazy.Function2;
import com.googlecode.totallylazy.None;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Segment;
import com.googlecode.totallylazy.comparators.Comparators;
import com.googlecode.totallylazy.iterators.EmptyIterator;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static com.googlecode.totallylazy.Unchecked.cast;

public class EmptyMap<K, V> implements ImmutableMap<K, V> {
    protected final Function2<K, V, ImmutableMap<K,V>> creator;

    protected EmptyMap(Callable2<? super K, ? super V, ? extends ImmutableMap<K, V>> creator) {
        this.creator = Function2.function(creator);
    }

    public static <K, V> EmptyMap<K, V> emptyMap(Callable2<K, V, ImmutableMap<K, V>> creator) {
        return new EmptyMap<K, V>(creator);
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
    public ImmutableMap<K, V> filterValues(Predicate<? super V> predicate) {
        return this;
    }

    @Override
    public <NewV> ImmutableMap<K, NewV> mapValues(Callable1<? super V, ? extends NewV> transformer) {
        return cast(this);
    }

    @Override
    public <C extends Segment<Pair<K, V>, C>> C joinTo(C rest) {
        return rest;
    }

    @Override
    public ImmutableMap<K, V> cons(Pair<K, V> newValue) {
        return creator.apply(newValue.first(), newValue.second());
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
