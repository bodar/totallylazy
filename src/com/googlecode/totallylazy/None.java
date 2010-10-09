package com.googlecode.totallylazy;

import com.googlecode.totallylazy.iterators.EmptyIterator;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class None<T> extends Option<T>{
    static final None Instance = new None();

    private None() {}

    public Iterator<T> iterator() {
        return new EmptyIterator<T>();
    }

    @Override
    public T get() {
        throw new NoSuchElementException();
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public String toString() {
        return "none()";
    }
}
