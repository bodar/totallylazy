package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.predicates.Predicate;

import java.util.Collection;

public abstract class ReadOnlyCollection<T> implements Collection<T> {
    /** @deprecated Mutation not supported. Replaced by {@link PersistentCollection#cons(T)} */
    @Override @Deprecated
    public boolean add(T e) {
        throw new IllegalMutationException();
    }

    /** @deprecated Mutation not supported. Replaced by {@link PersistentCollection#cons(T)} */
    @Override @Deprecated
    public boolean addAll(Collection<? extends T> c) {
        throw new IllegalMutationException();
    }

    /** @deprecated Mutation not supported. Replaced by {@link PersistentCollection#delete(T)} */
    @Override @Deprecated
    public boolean remove(Object o) {
        throw new IllegalMutationException();
    }

    /** @deprecated Mutation not supported. Replaced by {@link PersistentCollection#deleteAll(Iterable)} */
    @Override @Deprecated
    public boolean removeAll(Collection<?> c) {
        throw new IllegalMutationException();
    }

    /** @deprecated Mutation not supported. Replaced by {@link PersistentCollection#filter(Predicate)} */
    @Override @Deprecated
    public boolean retainAll(Collection<?> c) {
        throw new IllegalMutationException();
    }

    /** @deprecated Mutation not supported. Replaced by {@link PersistentCollection#empty()} */
    @Override @Deprecated
    public void clear() {
        throw new IllegalMutationException();
    }
}