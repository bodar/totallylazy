package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.*;
import com.googlecode.totallylazy.iterators.EmptyIterator;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static com.googlecode.totallylazy.None.none;
import static com.googlecode.totallylazy.Unchecked.cast;

public abstract class AbstractEmptyTreeMap<K, V, Self extends TreeMap<K, V>> extends AbstractMap<K, V> implements TreeMap<K, V> {
    protected final Comparator<K> comparator;

    protected final TreeFactory treeFactory;

    protected AbstractEmptyTreeMap(Comparator<K> comparator, TreeFactory treeFactory) {
        this.treeFactory = treeFactory;
        this.comparator = comparator;
    }

    @Override
    public TreeFactory factory() {
        return treeFactory;
    }

    @Override
    public Comparator<K> comparator() {
        return comparator;
    }

    @Override
    public PersistentList<Pair<K, V>> toPersistentList() {
        return PersistentList.constructors.empty();
    }

    @Override
    public Sequence<Pair<K, V>> toSequence() {
        return Sequences.empty();
    }

    @Override
    public None<V> lookup(K key) {
        return None.none();
    }

    @Override
    public Self insert(K key, V value) {
        return cons(Pair.pair(key, value));
    }

    @Override
    public None<V> find(Predicate<? super K> predicate) {
        return None.none();
    }

    @Override
    public Self filter(Predicate<? super Pair<K, V>> predicate) {
        return cast(this);
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
    public <S> S fold(S seed, Function2<? super S, ? super Pair<K, V>, ? extends S> callable) {
        return seed;
    }

    @Override
    public <NewV> TreeMap<K, NewV> map(Function1<? super V, ? extends NewV> transformer) {
        return cast(this);
    }

    @Override
    public Self delete(K key) {
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
    public Sequence<K> keys() {
        return Sequences.empty();
    }

    @Override
    public Sequence<V> values() {
        return Sequences.empty();
    }

    @Override
    public <C extends Segment<Pair<K, V>>> C joinTo(C rest) {
        return rest;
    }

    @Override
    public Self empty() {
        return cast(treeFactory.<K, V>create(comparator));
    }

    @Override
    public Self cons(Pair<K, V> newValue) {
        return cast(treeFactory.create(comparator, newValue.first(), newValue.second()));
    }

    @Override
    public boolean contains(Object other) {
        return false;
    }

    @Override
    public boolean exists(Predicate<? super K> predicate) {
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
    public Option<Pair<K, V>> headOption() {
        return none();
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
    public Pair<K, V> get(int i) {
        throw new IndexOutOfBoundsException();
    }

    @Override
    public int indexOf(Object pair) {
        return -1;
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

    @Override
    public Self rotateLeft() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Self rotateRight() {
        throw new UnsupportedOperationException();
    }
}
