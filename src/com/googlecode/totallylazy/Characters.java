package com.googlecode.totallylazy;

import com.googlecode.totallylazy.callables.JoinCharSequence;
import com.googlecode.totallylazy.numbers.Numbers;
import com.googlecode.totallylazy.predicates.LogicalPredicate;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.Set;

import static com.googlecode.totallylazy.Predicates.not;

public class Characters {
    public static Charset UTF8 = Charset.forName("UTF-8");
    public static Charset UTF16 = Charset.forName("UTF-16");
    public static Charset ASCII = Charset.forName("ASCII");

    public static final Combiner<CharSequence> join = JoinCharSequence.instance;

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

    public static LogicalPredicate<Character> in(final String characters) {
        return new LogicalPredicate<Character>() {
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

    public static LogicalPredicate<Character> among(String value) {
        return in(value);
    }

    public static LogicalPredicate<Character> notAmong(String value) {
        return not(in(value));
    }

    public static LogicalPredicate<Character> identifierStart = new LogicalPredicate<Character>() {
        @Override
        public boolean matches(Character other) {
            return Character.isJavaIdentifierStart(other);
        }
    };

    public static LogicalPredicate<Character> identifierPart = new LogicalPredicate<Character>() {
        @Override
        public boolean matches(Character other) {
            return Character.isJavaIdentifierPart(other);
        }
    };

    public static LogicalPredicate<Character> letter = new LogicalPredicate<Character>() {
        public boolean matches(Character other) {
            return Character.isLetter(other);
        }
    };

    public static LogicalPredicate<Character> digit = new LogicalPredicate<Character>() {
        public boolean matches(Character other) {
            return Character.isDigit(other);
        }
    };

    public static LogicalPredicate<Character> whitespace = new LogicalPredicate<Character>() {
        public boolean matches(Character other) {
            return Character.isWhitespace(other);
        }
    };

    public static LogicalPredicate<Character> between(final char start, final char end) {
        return new LogicalPredicate<Character>() {
            public boolean matches(Character other) {
                return other >= start && other <= end;
            }
        };
    }

    public static LogicalPredicate<Character> alphaNumeric = between('A', 'Z').or(between('a', 'z')).or(between('0', '9'));
    public static LogicalPredicate<Character> hexDigit = between('A', 'F').or(between('a', 'F')).or(between('0', '9'));

    public static Sequence<Character> range(char start, char end) {
        return Numbers.range((int) start, (int) end).map(new Function<Number, Character>() {
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
