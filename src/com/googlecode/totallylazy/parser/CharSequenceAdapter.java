package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.MemorisedSequence;
import com.googlecode.totallylazy.Sequence;

public class CharSequenceAdapter implements CharSequence {
    private final MemorisedSequence<Character> characters;

    public CharSequenceAdapter(Sequence<Character> characters) {
        this.characters = characters.memorise();
    }

    @Override
    public int length() {
        return Integer.MAX_VALUE;
    }

    @Override
    public char charAt(int index) {
        throw new UnsupportedOperationException("charAt " + index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        throw new UnsupportedOperationException("subSequence  " + start + " " + end);
    }
}
