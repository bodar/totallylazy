package com.googlecode.totallylazy;

import com.googlecode.totallylazy.callables.JoinCharSequence;
import com.googlecode.totallylazy.numbers.Numbers;

import java.nio.charset.Charset;
import java.util.Set;

public class Characters {
    public static Charset UTF8 = Charset.forName("UTF-8");
    public static Charset UTF16 = Charset.forName("UTF-16");
    public static Charset ASCII = Charset.forName("ASCII");

    public static final CombinerFunction<CharSequence> join = JoinCharSequence.instance;

    public static Sequence<Character> characters(final CharSequence value) {
        return Sequences.characters(value);
    }

    public static Sequence<Character> characters(final char[] value) {
        return Sequences.characters(value);
    }

    public static Predicate<Character> in(Charset charset) {
        return charset.newEncoder()::canEncode;
    }

    public static Predicate<Character> in(final String characters) {
        return new Predicate<Character>() {
            @Override
            public boolean matches(Character other) {
                return characters.indexOf(other) != -1;
            }

            @Override
            public String toString() {
                return "[" + characters + "]";
            }
        };
    }

    public static Predicate<Character> identifierStart = Character::isJavaIdentifierStart;

    public static Predicate<Character> identifierPart = Character::isJavaIdentifierPart;

    public static Predicate<Character> letter = Character::isLetter;

    public static Predicate<Character> digit = Character::isDigit;

    public static Predicate<Character> between(final char start, final char end) {
        return (other) -> other >= start && other <= end;
    }

    public static Predicate<Character> alphaNumeric = between('A', 'Z').or(between('a', 'z')).or(between('0', '9'));

    public static Sequence<Character> range(char start, char end) {
        return Numbers.range((int) start, (int) end).map(number -> (char) number.intValue());
    }

    public static Set<Character> set(Charset charset) {
        return characters(charset).toSet();
    }

    public static Sequence<Character> characters(Charset charset) {
        return characters().filter(in(charset));
    }

    public static Sequence<Character> characters() {
        return range(Character.MIN_VALUE, Character.MAX_VALUE);
    }
}
