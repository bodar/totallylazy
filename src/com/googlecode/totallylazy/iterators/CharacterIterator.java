package com.googlecode.totallylazy.iterators;

import java.util.NoSuchElementException;

public class CharacterIterator extends ReadOnlyIterator<Character> {
    private final char[] array;
    private int index = 0;

    public CharacterIterator(char[] array) {
        this.array = array;
    }

    public boolean hasNext() {
        return index < array.length;
    }

    public Character next() {
        if (hasNext()) {
            return array[index++];
        }
        throw new NoSuchElementException();
    }
}
