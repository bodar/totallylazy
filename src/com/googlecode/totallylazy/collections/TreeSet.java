package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callables;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Segment;
import com.googlecode.totallylazy.iterators.SegmentIterator;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static com.googlecode.totallylazy.Unchecked.cast;

public class TreeSet<T> implements ImmutableSet<T> {
    private final ImmutableSortedMap<T,T> map;

    private TreeSet(ImmutableSortedMap<T, T> map) {
        this.map = map;
    }

    public static <T> ImmutableSet<T> treeSet(ImmutableSortedMap<T, T> map) {
        return new TreeSet<T>(map);
    }

    private Pair<ImmutableSet<T>, T> treeSet(Pair<ImmutableSortedMap<T, T>, Pair<T, T>> pair) {
        return Pair.pair(treeSet(pair.first()), pair.second().first());
    }


    @Override
    public ImmutableList<T> immutableList() {
        return map.immutableList().map(Callables.<T>first());
    }

    @Override
    public ImmutableSet<T> put(T value) {
        return treeSet(map.put(value, value));
    }

    @Override
    public Option<T> find(Predicate<? super T> predicate) {
        return map.find(predicate);
    }

    @Override
    public ImmutableSet<T> filter(Predicate<? super T> predicate) {
        return treeSet(map.filterKeys(predicate));
    }

    @Override
    public <NewT> ImmutableSet<NewT> map(Callable1<? super T, ? extends NewT> transformer) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ImmutableSet<T> remove(T value) {
        return treeSet(map.remove(value));
    }

    @Override
    public Pair<ImmutableSet<T>, T> removeMinimum() {
        return treeSet(map.removeMinimum());
    }

    @Override
    public Pair<ImmutableSet<T>, T> removeMaximum() {
        return treeSet(map.removeMaximum());
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public T head() throws NoSuchElementException {
        return map.head().first();
    }

    @Override
    public Segment<T> tail() throws NoSuchElementException {
        return treeSet(map.tail());
    }

    @Override
    public ImmutableSet<T> cons(T head) {
        return treeSet(map.cons(Pair.pair(head, head)));
    }

    @Override
    public <C extends Segment<T>> C joinTo(C rest) {
        if(map.isEmpty()) return rest;
        return cast(tail().joinTo(rest).cons(head()));
    }

    @Override
    public boolean contains(T other) {
        return map.contains(other);
    }

    @Override
    public Iterator<T> iterator() {
        return new SegmentIterator<T>(immutableList());
    }
}
