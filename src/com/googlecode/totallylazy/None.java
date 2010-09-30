package com.googlecode.totallylazy;

import com.googlecode.totallylazy.iterators.NoneIterator;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class None<T> extends Option<T>{
    public Iterator<T> iterator() {
        return new NoneIterator<T>();
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
    public boolean equals(Object o) {
        return this == o || !(o == null || getClass() != o.getClass());
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public String toString() {
        return "None()";
    }

}
