package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Segment;
import com.googlecode.totallylazy.Sequence;

import java.util.Collection;
import java.util.NoSuchElementException;

public interface PersistentCollection<T> extends Container<T>, Collection<T>, Segment<T> {
    @Override
    PersistentCollection<T> empty();

    @Override
    PersistentCollection<T> cons(T t);

    @Override
    PersistentCollection<T> tail() throws NoSuchElementException;

    PersistentCollection<T> delete(T t);

    PersistentCollection<T> deleteAll(Iterable<? extends T> items);

    PersistentCollection<T> filter(Predicate<? super T> predicate);

    Sequence<T> toSequence();

    /** @deprecated Mutation not supported. Replaced by {@link PersistentCollection#cons(T)} */
    @Override @Deprecated
    boolean add(T e);

    /** @deprecated Mutation not supported. Replaced by {@link PersistentCollection#cons(T)} */
    @Override @Deprecated
    boolean addAll(Collection<? extends T> c);

    /** @deprecated Mutation not supported. Replaced by {@link PersistentCollection#delete(T)} */
    @Override @Deprecated
    boolean remove(Object o);

    /** @deprecated Mutation not supported. Replaced by {@link PersistentCollection#filter(Predicate)} */
    @Override @Deprecated
    boolean removeAll(Collection<?> c);

    /** @deprecated Mutation not supported. Replaced by {@link PersistentCollection#filter(Predicate)} */
    @Override @Deprecated
    boolean retainAll(Collection<?> c);

    /** @deprecated Mutation not supported. Replaced by {@link PersistentCollection#empty()} */
    @Override @Deprecated
    void clear();
}
