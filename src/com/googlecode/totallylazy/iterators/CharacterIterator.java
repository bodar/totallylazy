package com.googlecode.totallylazy.iterators;

import java.util.NoSuchElementException;

public class CharacterIterator extends ReadOnlyIterator<Character> {
    private int index = 0;
    private final CharSequence charSequence;

    public CharacterIterator(CharSequence charSequence) {
        this.charSequence = charSequence;
    }

    public boolean hasNext() {
        return index < charSequence.length();
    }

    public Character next() {
        if (hasNext()) {
            return charSequence.charAt(index++);
        }
        throw new NoSuchElementException();
    }
}
