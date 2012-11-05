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
import java.util.RandomAccess;

import static com.googlecode.totallylazy.Predicates.in;
import static com.googlecode.totallylazy.Predicates.not;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Sets.set;
import static com.googlecode.totallylazy.numbers.Numbers.intValue;

public class TreeList<T> implements ImmutableList<T>, RandomAccess {
    private final ImmutableSortedMap<Integer, T> map;

    private TreeList(ImmutableSortedMap<Integer, T> map) {this.map = map;}

    public static <T> TreeList<T> treeList(ImmutableSortedMap<Integer, T> map) {return new TreeList<T>(map);}

    public static <T> TreeList<T> treeList() {
        return treeList(ImmutableSortedMap.constructors.<Integer, T>emptySortedMap());
    }

    public static <T> TreeList<T> treeList(T first) {
        return treeList(sequence(first));
    }

    public static <T> TreeList<T> treeList(T first, T second) {
        return treeList(sequence(first, second));
    }

    public static <T> TreeList<T> treeList(T first, T second, T third) {
        return treeList(sequence(first, second, third));
    }

    public static <T> TreeList<T> treeList(T first, T second, T third, T fourth) {
        return treeList(sequence(first, second, third, fourth));
    }

    public static <T> TreeList<T> treeList(T first, T second, T third, T fourth, T fifth) {
        return treeList(sequence(first, second, third, fourth, fifth));
    }

    public static <T> TreeList<T> treeList(T... values) {
        return treeList(sequence(values));
    }

    public static <T> TreeList<T> treeList(Iterable<? extends T> iterable) {
        return treeList(ImmutableSortedMap.constructors.<Integer, T>sortedMap(sequence(iterable).
                zipWithIndex().map(Callables.<Number, T, Integer>first(intValue))));
    }

    @Override
    public <S> TreeList<S> map(Callable1<? super T, ? extends S> callable) {
        return treeList(toSequence().map(callable));
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
    public TreeList<T> tail() throws NoSuchElementException {
        return treeList(map.remove(map.first().first()));
    }

    @Override
    public TreeList<T> cons(T head) {
        int index = map.isEmpty() ? 0 : map.first().first();
        return treeList(map.cons(Pair.pair(--index, head)));
    }

    @Override
    public TreeList<T> add(T value) {
        int index = map.isEmpty() ? 0 : map.last().first();
        return treeList(map.cons(Pair.pair(++index, value)));
    }

    @Override
    public TreeList<T> remove(T value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public TreeList<T> removeAll(Iterable<T> values) {
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
    public TreeList<T> filter(Predicate<? super T> predicate) {
        return treeList(toSequence().filter(predicate));
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
