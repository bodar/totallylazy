package com.googlecode.totallylazy.iterators;

import java.util.NoSuchElementException;

public final class CharacterIterator extends ReadOnlyIterator<Character> {
    private int index = 0;
    private final CharSequence charSequence;

    public CharacterIterator(final CharSequence charSequence) {
        this.charSequence = charSequence;
    }

    public final boolean hasNext() {
        return index < charSequence.length();
    }

    public final Character next() {
        if (hasNext()) {
            return charSequence.charAt(index++);
        }
        throw new NoSuchElementException();
    }
}
