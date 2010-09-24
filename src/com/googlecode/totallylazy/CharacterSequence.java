package com.googlecode.totallylazy;

import com.googlecode.totallylazy.iterators.CharacterIterator;

import java.util.Iterator;

class CharacterSequence extends Sequence<Character> {
    private final String value;

    public CharacterSequence(String value) {
        this.value = value;
    }

    public Iterator<Character> iterator() {
        return new CharacterIterator(value.toCharArray());
    }

    @Override
    public String toString() {
        return toString("");
    }
}
