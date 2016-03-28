package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.functions.Function1;
import com.googlecode.totallylazy.functions.Function2;
import com.googlecode.totallylazy.functions.Callables;
import com.googlecode.totallylazy.functions.Curried2;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.predicates.Predicate;
import com.googlecode.totallylazy.Segment;
import com.googlecode.totallylazy.Unchecked;
import com.googlecode.totallylazy.iterators.SegmentIterator;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

import static com.googlecode.totallylazy.Option.some;
import static com.googlecode.totallylazy.Unchecked.cast;

public class TreeSet<T> extends AbstractCollection<T> implements PersistentSortedSet<T> {
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
    public PersistentList<T> toPersistentList() {
        return map.toSequence().map(Callables.<T>first()).toPersistentList();
    }

    @Override
    public Option<T> lookup(T value) {
        return map.lookup(value);
    }

    @Override
    public PersistentSortedSet<T> delete(T value) {
        return treeSet(map.delete(value));
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
    public Set<T> toSet() {
        return map.keys().toSet();
    }

    @Override
    public T get(int i) {
        return map.get(i).first();
    }

    @Override
    public int indexOf(Object t) {
        return map.indexOf(pair(t));
    }

    @Override
    public PersistentSortedSet<T> empty() {
        return treeSet(map.empty());
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
    public PersistentSortedSet<T> tail() throws NoSuchElementException {
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
    public boolean contains(Object other) {
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
        return SegmentIterator.iterator(toPersistentList());
    }

    private Pair<T, T> pair(Object head) {
        return Pair.pair(Unchecked.<T>cast(head), Unchecked.<T>cast(head));
    }

    @Override
    public <S> S fold(S seed, final Function2<? super S, ? super T, ? extends S> callable) {
        return map.fold(seed, new Curried2<S, Pair<?, T>, S>() {
            @Override
            public S call(S s, Pair<?, T> pair) throws Exception {
                return callable.call(s, pair.second());
            }
        });
    }
}
