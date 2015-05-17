package com.googlecode.totallylazy.segments;

import com.googlecode.totallylazy.Segment;

import java.util.NoSuchElementException;

public class CharacterSegment extends AbstractSegment<Character> {
    private final CharSequence charSequence;
    private final int offset;

    private CharacterSegment(CharSequence charSequence, int offset) {
        this.charSequence = charSequence;
        this.offset = offset;
    }

    public static Segment<Character> characterSegment(CharSequence charSequence) {
        return new CharacterSegment(charSequence, 0);
    }

    @Override
    public boolean isEmpty() {
        return charSequence.length() <= offset;
    }

    @Override
    public Character head() throws NoSuchElementException {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return charSequence.charAt(offset);
    }

    @Override
    public Segment<Character> tail() throws NoSuchElementException {
        if (isEmpty()) throw new NoSuchElementException();
        return new CharacterSegment(charSequence, offset + 1);
    }

    @Override
    public String toString() {
        return charSequence.subSequence(offset, charSequence.length()).toString();
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Segment && toString().equals(obj.toString());
    }
}
