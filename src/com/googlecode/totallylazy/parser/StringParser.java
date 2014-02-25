package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Function1;
import com.googlecode.totallylazy.Segment;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;

import static com.googlecode.totallylazy.Sequences.characters;
import static com.googlecode.totallylazy.parser.SequenceParser.sequenceOf;
import static com.googlecode.totallylazy.parser.Success.success;

public class StringParser extends Parser<String> {
    private final String expected;

    private StringParser(String expected) {
        this.expected = expected;
    }

    public static StringParser string(String value) {
        return new StringParser(value);
    }

    @Override
    public Result<String> parse(Segment<Character> input) throws Exception {
        Segment<Character> segment = input;
        for (int i = 0; i < expected.length(); i++) {
            char e = expected.charAt(i);
            char a = segment.head();
            if(e != a) return fail(e, a);
            segment = segment.tail();
        }
        return success(expected, segment);
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
