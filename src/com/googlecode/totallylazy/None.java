package com.googlecode.totallylazy;

import com.googlecode.totallylazy.iterators.EmptyIterator;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class None<T> extends Option<T>{
    private None() {}

    public static <T> None<T> none() {
        return new None<T>();
    }

    public static <T> None<T> none(Class<T> aClass) {
        return none();
    }

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

    @Override
    public int hashCode() {
        return 19;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof None;
    }
}
