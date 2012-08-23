package com.googlecode.totallylazy;

import com.googlecode.totallylazy.numbers.Numbers;
import com.googlecode.totallylazy.predicates.LogicalPredicate;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.Set;

public class Characters {
    public static Charset UTF8 = Charset.forName("UTF-8");
    public static Charset UTF16 = Charset.forName("UTF-16");
    public static Charset ASCII = Charset.forName("ASCII");

    public static Sequence<Character> characters(final CharSequence value) {
        return Sequences.characters(value);
    }

    public static Sequence<Character> characters(final char[] value) {
        return Sequences.characters(value);
    }

    public static LogicalPredicate<Character> in(Charset charset) {
        final CharsetEncoder encoder = charset.newEncoder();
        return new LogicalPredicate<Character>() {
            @Override
            public boolean matches(Character other) {
                return encoder.canEncode(other);
            }
        };
    }

    public static LogicalPredicate<Character> identifierStart() {
        return new LogicalPredicate<Character>() {
            @Override
            public boolean matches(Character other) {
                return Character.isJavaIdentifierStart(other);
            }
        };
    }

    public static LogicalPredicate<Character> identifierPart() {
        return new LogicalPredicate<Character>() {
            @Override
            public boolean matches(Character other) {
                return Character.isJavaIdentifierPart(other);
            }
        };
    }

    public static Sequence<Character> range(char start, char end) {
        return Numbers.range((int) start, (int) end).map(new Callable1<Number, Character>() {
            @Override
            public Character call(Number number) throws Exception {
                return (char) number.intValue();
            }
        });
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