package com.googlecode.totallylazy.segments;

import com.googlecode.totallylazy.Segment;

import java.util.NoSuchElementException;

import static com.googlecode.totallylazy.Unchecked.cast;

public class CharacterSegment extends AbstractSegment<Character> {
    private final CharSequence charSequence;
    private final int offset;

    private CharacterSegment(CharSequence charSequence, int offset) {
        this.charSequence = charSequence;
        this.offset = offset;
    }

    public static Segment<Character> characterSegment(CharSequence charSequence) {
        return characterSegment(charSequence, 0);
    }

    public static Segment<Character> characterSegment(CharSequence charSequence, int offset) {
//        if(charSequence.length() == offset) return Segment.constructors.emptySegment();
        return new CharacterSegment(charSequence, offset);
    }

    @Override
    public boolean isEmpty() {
        return offset == charSequence.length();
    }

    @Override
    public Character head() throws NoSuchElementException {
        return charSequence.charAt(offset);
    }

    @Override
    public Segment<Character> tail() throws NoSuchElementException {
        return characterSegment(charSequence, offset + 1);
    }

    @Override
    public Segment<Character> cons(Character head) {
        return constructors.segment(head, this);
    }

    @Override
    public <C extends Segment<Character>> C joinTo(C rest) {
        return cast(tail().joinTo(rest).cons(head()));
    }
}
