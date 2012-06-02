package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Segment;
import com.googlecode.totallylazy.comparators.Comparators;
import com.googlecode.totallylazy.iterators.EmptyIterator;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class EmptySet<T> implements ImmutableSet<T> {
    final Comparator<T> comparator;

    EmptySet(Comparator<T> comparator) {
        this.comparator = comparator;
    }

    public static <T extends Comparable<? super T>> EmptySet<T> empty(Class<T> aClass) {
        return EmptySet.<T>empty();
    }

    public static <T extends Comparable<? super T>> EmptySet<T> empty() {
        return empty(Comparators.<T>ascending());
    }

    public static <T> EmptySet<T> empty(Comparator<T> comparator) {
        return new EmptySet<T>(comparator);
    }

    @Override
    public ImmutableList<T> immutableList() {
        return ImmutableList.constructors.empty();
    }

    @Override
    public <C extends Segment<T, C>> C joinTo(C rest) {
        return rest;
    }

    @Override
    public ImmutableSet<T> cons(T newValue) {
        return ImmutableSet.constructors.set(newValue, comparator);
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
        return obj instanceof EmptySet;
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
    public ImmutableSet<T> tail() throws NoSuchElementException {
        throw new NoSuchElementException();
    }

    @Override
    public Iterator<T> iterator() {
        return new EmptyIterator<T>();
    }
}
