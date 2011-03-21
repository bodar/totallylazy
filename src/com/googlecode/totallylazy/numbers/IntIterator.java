package com.googlecode.totallylazy.numbers;

import com.googlecode.totallylazy.iterators.ReadOnlyIterator;

import java.util.NoSuchElementException;

public final class IntIterator extends ReadOnlyIterator<Number> {
    private final int[] array;
    private int index = 0;

    public IntIterator(final int[] array) {
        this.array = array;
    }

    public final boolean hasNext() {
        return index < array.length;
    }

    public final Number next() {
        if(hasNext()){
            return (Number) array[index++];
        }
        throw new NoSuchElementException();
    }
}
