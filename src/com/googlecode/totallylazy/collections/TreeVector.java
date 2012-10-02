package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callables;
import com.googlecode.totallylazy.Iterators;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Segment;
import com.googlecode.totallylazy.Sequence;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import static com.googlecode.totallylazy.Predicates.in;
import static com.googlecode.totallylazy.Predicates.not;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Sets.set;
import static com.googlecode.totallylazy.collections.ImmutableVector.constructors.vector;

public class TreeVector<T> implements ImmutableVector<T> {
    private final ImmutableSortedMap<Integer, T> map;

    private TreeVector(ImmutableSortedMap<Integer, T> map) {this.map = map;}

    public static <T> TreeVector<T> treeVector(ImmutableSortedMap<Integer, T> map) {return new TreeVector<T>(map);}

    @Override
    public <S> ImmutableVector<S> map(Callable1<? super T, ? extends S> callable) {
        return vector(toSequence().map(callable));
    }

    @Override
    public Option<T> find(Predicate<? super T> predicate) {
        return toSequence().find(predicate);
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public T head() throws NoSuchElementException {
        if(map.isEmpty()) throw new NoSuchElementException();
        return map.index(0).second();
    }

    @Override
    public ImmutableVector<T> tail() throws NoSuchElementException {
        return treeVector(map.remove(map.first().first()));
    }

    @Override
    public ImmutableVector<T> cons(T head) {
        int index = map.isEmpty() ? 0 : map.first().first();
        return treeVector(map.cons(Pair.pair(--index, head)));
    }

    @Override
    public ImmutableVector<T> add(T value) {
        int index = map.isEmpty() ? 0 : map.last().first();
        return treeVector(map.cons(Pair.pair(++index, value)));
    }

    @Override
    public ImmutableVector<T> remove(T value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ImmutableVector<T> removeAll(Iterable<T> values) {
        return filter(not(in(set(values))));
    }

    @Override
    public List<T> toList() {
        return toSequence().toList();
    }

    @Override
    public Sequence<T> toSequence() {
        return sequence(this);
    }

    @Override
    public ImmutableVector<T> filter(Predicate<? super T> predicate) {
        return ImmutableVector.constructors.vector(toSequence().filter(predicate));
    }

    @Override
    public <C extends Segment<T>> C joinTo(C rest) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean contains(T other) {
        return toSequence().contains(other);
    }

    @Override
    public boolean exists(Predicate<? super T> predicate) {
        return toSequence().exists(predicate);
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public T index(int i) throws IndexOutOfBoundsException {
        return map.index(i).second();
    }

    @Override
    public int indexOf(T t) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<T> iterator() {
        return Iterators.map(map.iterator(), Callables.<T>second());
    }
}
