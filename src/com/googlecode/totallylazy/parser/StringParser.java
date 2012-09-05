package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Function1;
import com.googlecode.totallylazy.Segment;
import com.googlecode.totallylazy.Sequence;

import static com.googlecode.totallylazy.Sequences.characters;
import static com.googlecode.totallylazy.parser.SequenceParser.sequenceOf;

public class StringParser extends AbstractParser<String> {
    private final CharSequence expected;
    private final SequenceParser<Character> parser;

    private StringParser(CharSequence expected) {
        this.expected = expected;
        parser = sequenceOf(characters(expected).map(CharacterParser.characterParser()));
    }

    public static StringParser string(CharSequence value) {
        return new StringParser(value);
    }

    @Override
    public Result<String> parse(Segment<Character> input) throws Exception {
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
        return expected.toString();
    }
}
