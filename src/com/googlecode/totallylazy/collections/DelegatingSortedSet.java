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

public class DelegatingSortedSet<T> implements ImmutableSet<T> {
    private final ImmutableSortedMap<T,T> map;

    private DelegatingSortedSet(ImmutableSortedMap<T, T> map) {
        this.map = map;
    }

    public static <T> ImmutableSet<T> sortedSet(ImmutableSortedMap<T, T> map) {
        return new DelegatingSortedSet<T>(map);
    }

    private Pair<ImmutableSet<T>, T> sortedSet(Pair<ImmutableSortedMap<T, T>, Pair<T, T>> pair) {
        return Pair.pair(sortedSet(pair.first()), pair.second().first());
    }


    @Override
    public ImmutableList<T> immutableList() {
        return map.immutableList().map(Callables.<T>first());
    }

    @Override
    public ImmutableSet<T> put(T value) {
        return sortedSet(map.put(value, value));
    }

    @Override
    public Option<T> find(Predicate<? super T> predicate) {
        return map.find(predicate);
    }

    @Override
    public ImmutableSet<T> filter(Predicate<? super T> predicate) {
        return sortedSet(map.filterKeys(predicate));
    }

    @Override
    public <NewT> ImmutableSet<NewT> map(Callable1<? super T, ? extends NewT> transformer) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ImmutableSet<T> remove(T value) {
        return sortedSet(map.remove(value));
    }

    @Override
    public Pair<ImmutableSet<T>, T> removeMinimum() {
        return sortedSet(map.removeMinimum());
    }

    @Override
    public Pair<ImmutableSet<T>, T> removeMaximum() {
        return sortedSet(map.removeMaximum());
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
        return sortedSet(map.tail());
    }

    @Override
    public ImmutableSet<T> cons(T head) {
        return sortedSet(map.cons(Pair.pair(head, head)));
    }

    @Override
    public <C extends Segment<T>> C joinTo(C rest) {
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
