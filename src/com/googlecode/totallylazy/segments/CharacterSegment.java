package com.googlecode.totallylazy.segments;

import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Segment;

import java.util.NoSuchElementException;

import static com.googlecode.totallylazy.None.none;
import static com.googlecode.totallylazy.Option.some;
import static com.googlecode.totallylazy.Unchecked.cast;

public class CharacterSegment extends AbstractSegment<Character> {
    private final CharSequence charSequence;

    private CharacterSegment(CharSequence charSequence) {
        this.charSequence = charSequence;
    }

    public static Segment<Character> characterSegment(CharSequence charSequence) {
        return new CharacterSegment(charSequence);
    }

    @Override
    public boolean isEmpty() {
        return charSequence.length() == 0;
    }

    @Override
    public Character head() throws NoSuchElementException {
        try {
            return charSequence.charAt(0);
        } catch (IndexOutOfBoundsException e) {
            throw new NoSuchElementException();
        }
    }

    @Override
    public Option<Character> headOption() {
        return isEmpty()
                ? none(Character.class)
                : some(head());
    }

    @Override
    public Segment<Character> tail() throws NoSuchElementException {
        try {
            return characterSegment(charSequence.subSequence(1, charSequence.length()));
        } catch (IndexOutOfBoundsException e) {
            throw new NoSuchElementException();
        }
    }

    @Override
    public Segment<Character> cons(Character head) {
        return constructors.segment(head, this);
    }

    @Override
    public <C extends Segment<Character>> C joinTo(C rest) {
        return cast(tail().joinTo(rest).cons(head()));
    }

    @Override
    public String toString() {
        return charSequence.toString();
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
