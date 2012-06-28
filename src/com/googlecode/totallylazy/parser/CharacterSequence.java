package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Segment;

public class CharacterSequence implements CharSequence {
    private final StringBuilder builder = new StringBuilder();
    private Segment<Character> current;

    public CharacterSequence(Segment<Character> characters) {
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
        if (index < builder.length()) return builder.charAt(index);

        for (int i = 0; i < index - builder.length(); i++) {
            builder.append(current.head());
            current = current.tail();
        }
        return current.head();
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        StringBuilder result = new StringBuilder();
        for (int i = start; i < end; i++) {
            result.append(charAt(i));
        }
        return result.toString();
    }

    @Override
    public String toString() {
        return builder.toString() + current.head();
    }

    public Segment<Character> current() {
        return current;
    }
}
