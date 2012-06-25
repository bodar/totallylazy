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

public class TreeSet<T> implements ImmutableSortedSet<T> {
    private final ImmutableSortedMap<T, T> map;

    private TreeSet(ImmutableSortedMap<T, T> map) {
        this.map = map;
    }

    public static <T> ImmutableSortedSet<T> treeSet(ImmutableSortedMap<T, T> map) {
        return new TreeSet<T>(map);
    }

    private Pair<ImmutableSortedSet<T>, T> treeSet(Pair<? extends ImmutableSortedMap<T, T>, Pair<T, T>> pair) {
        return Pair.pair(treeSet(pair.first()), pair.second().first());
    }

    @Override
    public ImmutableList<T> immutableList() {
        return map.immutableList().map(Callables.<T>first());
    }

    @Override
    public ImmutableSortedSet<T> put(T value) {
        return treeSet(map.put(value, value));
    }

    @Override
    public Option<T> find(Predicate<? super T> predicate) {
        return map.find(predicate);
    }

    @Override
    public ImmutableSortedSet<T> filter(Predicate<? super T> predicate) {
        return treeSet(map.filterKeys(predicate));
    }

    @Override
    public <NewT> ImmutableSortedSet<NewT> map(Callable1<? super T, ? extends NewT> transformer) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ImmutableSortedSet<T> remove(T value) {
        return treeSet(map.remove(value));
    }

    @Override
    public T first() throws NoSuchElementException {
        return map.first().first();
    }

    @Override
    public T last() throws NoSuchElementException {
        return map.last().first();
    }

    @Override
    public Pair<ImmutableSortedSet<T>, T> removeFirst() {
        return treeSet(map.removeFirst());
    }

    @Override
    public Pair<ImmutableSortedSet<T>, T> removeLast() {
        return treeSet(map.removeLast());
    }

    @Override
    public T index(int i) {
        return map.index(i).first();
    }

    @Override
    public int indexOf(T t) {
        return map.indexOf(pair(t));
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
    public ImmutableSortedSet<T> cons(T head) {
        return treeSet(map.cons(pair(head)));
    }

    @Override
    public <C extends Segment<T>> C joinTo(C rest) {
        if (map.isEmpty()) return rest;
        return cast(tail().joinTo(rest).cons(head()));
    }

    @Override
    public boolean contains(T other) {
        return map.contains(other);
    }

    @Override
    public boolean exists(Predicate<? super T> predicate) {
        return map.exists(predicate);
    }

    @Override
    public Iterator<T> iterator() {
        return new SegmentIterator<T>(immutableList());
    }

    private Pair<T, T> pair(T head) {
        return Pair.pair(head, head);
    }
}
