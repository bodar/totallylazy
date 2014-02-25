package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Segment;

public class CharacterSequence implements CharSequence {
    private final StringBuilder buffer = new StringBuilder();
    private Segment<Character> current;

    private CharacterSequence(Segment<Character> characters) {
        this.current = characters;
    }

    public static CharacterSequence charSequence(final Segment<Character> characters) {
        return new CharacterSequence(characters);
    }

    @Override
    public int length() {
        return Integer.MAX_VALUE;
    }

    @Override
    public char charAt(int index) {
        if (contains(index)) return buffer.charAt(index);

        for (int i = buffer.length(); i < index + 1; i++) {
            buffer.append(current.head());
            current = current.tail();
        }
        return buffer.charAt(index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        int index = end - 1;
        if(!contains(index)) charAt(index);
        return buffer.subSequence(start, end);
    }

    @Override
    public String toString() {
        return buffer.toString();
    }

    public Segment<Character> remainder() {
        return current;
    }

    private boolean contains(int index) {
        return index < buffer.length();
    }
}
