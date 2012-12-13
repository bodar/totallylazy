package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callable2;
import com.googlecode.totallylazy.Callables;
import com.googlecode.totallylazy.Function2;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Segment;
import com.googlecode.totallylazy.iterators.SegmentIterator;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static com.googlecode.totallylazy.Option.some;
import static com.googlecode.totallylazy.Unchecked.cast;

public class TreeSet<T> implements PersistentSortedSet<T> {
    private final PersistentSortedMap<T, T> map;

    private TreeSet(PersistentSortedMap<T, T> map) {
        this.map = map;
    }

    public static <T> PersistentSortedSet<T> treeSet(PersistentSortedMap<T, T> map) {
        return new TreeSet<T>(map);
    }

    private Pair<PersistentSortedSet<T>, T> treeSet(Pair<? extends PersistentSortedMap<T, T>, Pair<T, T>> pair) {
        return Pair.pair(treeSet(pair.first()), pair.second().first());
    }

    @Override
    public PersistentList<T> immutableList() {
        return map.immutableList().map(Callables.<T>first());
    }

    @Override
    public PersistentSortedSet<T> put(T value) {
        return treeSet(map.put(value, value));
    }

    @Override
    public Option<T> find(Predicate<? super T> predicate) {
        return map.find(predicate);
    }

    @Override
    public PersistentSortedSet<T> filter(Predicate<? super T> predicate) {
        return treeSet(map.filterKeys(predicate));
    }

    @Override
    public <NewT> PersistentSortedSet<NewT> map(Callable1<? super T, ? extends NewT> transformer) {
        throw new UnsupportedOperationException();
    }

    @Override
    public PersistentSortedSet<T> remove(T value) {
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
    public Pair<PersistentSortedSet<T>, T> removeFirst() {
        return treeSet(map.removeFirst());
    }

    @Override
    public Pair<PersistentSortedSet<T>, T> removeLast() {
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
    public Option<T> headOption() {
        return isEmpty()
                ? Option.<T>none()
                : some(head());
    }

    @Override
    public Segment<T> tail() throws NoSuchElementException {
        return treeSet(map.tail());
    }

    @Override
    public PersistentSortedSet<T> cons(T head) {
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
    public int size() {
        return map.size();
    }

    @Override
    public Iterator<T> iterator() {
        return SegmentIterator.iterator(immutableList());
    }

    private Pair<T, T> pair(T head) {
        return Pair.pair(head, head);
    }

    @Override
    public <S> S fold(S seed, final Callable2<? super S, ? super T, ? extends S> callable) {
        return map.fold(seed, new Function2<S, Pair<?, T>, S>() {
            @Override
            public S call(S s, Pair<?, T> pair) throws Exception {
                return callable.call(s, pair.second());
            }
        });
    }
}
