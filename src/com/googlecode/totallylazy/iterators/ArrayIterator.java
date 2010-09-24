package com.googlecode.totallylazy.iterators;

import java.util.NoSuchElementException;

public class ArrayIterator<T> extends ReadOnlyIterator<T> {
    private final T[] array;
    private int index = 0;

    public ArrayIterator(T[] array) {
        this.array = array;
    }

    public boolean hasNext() {
        return index < array.length;
    }

    public T next() {
        if(hasNext()){
            return array[index++];
        }
        throw new NoSuchElementException();
    }
}
