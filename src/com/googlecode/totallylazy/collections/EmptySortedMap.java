package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callable2;
import com.googlecode.totallylazy.Function2;
import com.googlecode.totallylazy.None;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Segment;
import com.googlecode.totallylazy.iterators.EmptyIterator;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static com.googlecode.totallylazy.Unchecked.cast;

public class EmptySortedMap<K, V> implements ImmutableSortedMap<K, V> {
    protected final Function2<K, V, ImmutableSortedMap<K,V>> creator;

    protected EmptySortedMap(Callable2<? super K, ? super V, ? extends ImmutableSortedMap<K, V>> creator) {
        this.creator = Function2.function(creator);
    }

    public static <K, V> EmptySortedMap<K, V> emptyMap(Callable2<? super K, ? super V, ? extends ImmutableSortedMap<K, V>> creator) {
        return new EmptySortedMap<K, V>(creator);
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
    public ImmutableSortedMap<K, V> put(K key, V value) {
        return cons(Pair.pair(key, value));
    }

    @Override
    public None<V> find(Predicate<? super K> predicate) {
        return None.none();
    }

    @Override
    public ImmutableSortedMap<K, V> filterKeys(Predicate<? super K> predicate) {
        return this;
    }

    @Override
    public ImmutableSortedMap<K, V> filterValues(Predicate<? super V> predicate) {
        return this;
    }

    @Override
    public <NewV> ImmutableSortedMap<K, NewV> mapValues(Callable1<? super V, ? extends NewV> transformer) {
        return cast(this);
    }

    @Override
    public ImmutableSortedMap<K, V> remove(K key) {
        return this;
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
    public Pair<ImmutableSortedMap<K, V>, Pair<K,V>> removeFirst() {
        throw new NoSuchElementException();
    }

    @Override
    public Pair<ImmutableSortedMap<K, V>, Pair<K,V>> removeLast() {
        throw new NoSuchElementException();
    }

    @Override
    public <C extends Segment<Pair<K, V>>> C joinTo(C rest) {
        return rest;
    }

    @Override
    public ImmutableSortedMap<K, V> cons(Pair<K, V> newValue) {
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
        return obj instanceof EmptySortedMap;
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
    public ImmutableSortedMap<K, V> tail() throws NoSuchElementException {
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

}
