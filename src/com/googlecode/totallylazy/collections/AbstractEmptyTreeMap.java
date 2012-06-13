package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callable2;
import com.googlecode.totallylazy.Function2;
import com.googlecode.totallylazy.None;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Segment;
import com.googlecode.totallylazy.iterators.EmptyIterator;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static com.googlecode.totallylazy.Unchecked.cast;

public abstract class AbstractEmptyTreeMap<K, V, Self extends TreeMap<K, V>> implements TreeMap<K, V> {
    protected final Comparator<K> comparator;
    protected final TreeFactory treeFactory;

    protected AbstractEmptyTreeMap(Comparator<K> comparator, TreeFactory treeFactory) {
        this.treeFactory = treeFactory;
        this.comparator = comparator;
    }

    @Override
    public Comparator<K> comparator() {
        return comparator;
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
    public Self put(K key, V value) {
        return cons(Pair.pair(key, value));
    }

    @Override
    public None<V> find(Predicate<? super K> predicate) {
        return None.none();
    }

    @Override
    public Self filterKeys(Predicate<? super K> predicate) {
        return cast(this);
    }

    @Override
    public Self filterValues(Predicate<? super V> predicate) {
        return cast(this);
    }

    @Override
    public <NewV> TreeMap<K, NewV> mapValues(Callable1<? super V, ? extends NewV> transformer) {
        return cast(this);
    }

    @Override
    public Self remove(K key) {
        return cast(this);
    }

    @Override
    public Pair<K, V> first() throws NoSuchElementException {
        throw new NoSuchElementException();
    }

    @Override
    public Pair<K, V> last() throws NoSuchElementException {
        throw new NoSuchElementException();
    }

    @Override
    public Pair<Self, Pair<K, V>> removeFirst() {
        throw new NoSuchElementException();
    }

    @Override
    public Pair<Self, Pair<K, V>> removeLast() {
        throw new NoSuchElementException();
    }

    @Override
    public <C extends Segment<Pair<K, V>>> C joinTo(C rest) {
        return rest;
    }

    @Override
    public Self cons(Pair<K, V> newValue) {
        return cast(treeFactory.create(comparator, newValue.first(), newValue.second()));
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
        return obj instanceof AbstractEmptyTreeMap;
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
    public Self tail() throws NoSuchElementException {
        throw new NoSuchElementException();
    }

    @Override
    public Iterator<Pair<K, V>> iterator() {
        return new EmptyIterator<Pair<K, V>>();
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public Pair<K, V> index(int i) {
        throw new IndexOutOfBoundsException();
    }

    @Override
    public K key() {
        throw new UnsupportedOperationException();
    }

    @Override
    public V value() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Self left() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Self left(TreeMap<K, V> newLeft) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Self right() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Self right(TreeMap<K, V> newRight) {
        throw new UnsupportedOperationException();
    }
}
