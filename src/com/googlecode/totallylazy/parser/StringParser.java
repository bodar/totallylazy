package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Function1;
import com.googlecode.totallylazy.Segment;
import com.googlecode.totallylazy.Sequence;

import static com.googlecode.totallylazy.Sequences.characters;
import static com.googlecode.totallylazy.parser.SequenceParser.sequenceOf;

public class StringParser extends BaseParser<String> {
    private final String expected;
    private final SequenceParser<Character> parser;

    private StringParser(String expected) {
        this.expected = expected;
        parser = sequenceOf(characters(expected).map(CharacterParser.characterParser()));
    }

    public static StringParser string(String value) {
        return new StringParser(value);
    }

    @Override
    public Result<String> parse(Segment<Character> input) throws Exception {
        return parser.parse(input).map(new Function1<Sequence<Character>, String>() {
            @Override
            public String call(Sequence<Character> characters) throws Exception {
                return characters.toString("");
            }
        });
    }

    @Override
    public String toString() {
        return expected;
    }
}
