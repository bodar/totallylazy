package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Function1;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;

import java.nio.CharBuffer;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.parser.SequenceParser.sequenceOf;

public class StringParser extends Parser<String> {
    private final SequenceParser<Character> parser;

    private StringParser(Iterable<? extends Parse<Character>> parsers) {
        parser = sequenceOf(parsers);
    }

    public static StringParser string(CharSequence value) {
        return string(Sequences.characters(value).map(CharacterParser.characterParser()));
    }

    public static StringParser string(Iterable<? extends Parse<Character>> map) {return new StringParser(map);}

    public static StringParser string(final Parse<Character> a, final Parse<Character> b) {
        return string(sequence(a, b));
    }

    public static StringParser string(final Parse<Character> a, final Parse<Character> b, final Parse<Character> c) {
        return string(sequence(a, b, c));
    }

    public static StringParser string(final Parse<Character> a, final Parse<Character> b, final Parse<Character> c, final Parse<Character> d) {
        return string(sequence(a, b, c, d));
    }

    public static StringParser string(final Parse<Character> a, final Parse<Character> b, final Parse<Character> c, final Parse<Character> d, final Parse<Character> e) {
        return string(sequence(a, b, c, d, e));
    }

    public static StringParser string(final Parse<Character>... parsers) {
        return string(sequence(parsers));
    }

    @Override
    public Result<String> parse(CharBuffer input) throws Exception {
        return parser.parse(input).map(asString(""));
    }

    public static Function1<Sequence<Character>, String> asString(final String separator) {
        return new Function1<Sequence<Character>, String>() {
            @Override
            public String call(Sequence<Character> characters) throws Exception {
                return characters.toString(separator);
            }
        };
    }

    @Override
    public String toString() {
        return parser.toString("");
    }
}
