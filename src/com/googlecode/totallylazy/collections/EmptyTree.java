package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Constructable;
import com.googlecode.totallylazy.comparators.Comparators;

import java.util.Comparator;
import java.util.NoSuchElementException;

class EmptyTree<T> extends LLRBTree<T> {
    public static <T extends Comparable<? super T>> LLRBTree<T> empty(Class<T> aClass) {
        return EmptyTree.<T>empty();
    }

    public static <T extends Comparable<? super T>> LLRBTree<T> empty() {
        return empty(Comparators.<T>ascending());
    }

    public static <T> LLRBTree<T> empty(Comparator<T> comparator) {
        return new EmptyTree<T>(comparator);
    }

    EmptyTree(Comparator<T> comparator) {
        super(comparator);
    }

    @Override
    public PersistentList<T> persistentList() {
        return PersistentList.empty();
    }

    @Override
    public <C extends Constructable<T, C>> C join(C rest) {
        return rest;
    }

    @Override
    public PersistentSet<T> cons(T newValue) {
        return red(this, newValue, this, comparator);
    }

    @Override
    public boolean contains(T other) {
        return false;
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof EmptyTree;
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
    public T head() throws NoSuchElementException {
        throw new NoSuchElementException();
    }

    @Override
    public PersistentSet<T> tail() throws NoSuchElementException {
        throw new NoSuchElementException();
    }
}
