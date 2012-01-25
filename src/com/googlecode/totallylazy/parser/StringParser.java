package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Sequence;

import static com.googlecode.totallylazy.parser.Success.success;

public class StringParser extends AbstractParser<String> {
    private final String expected;

    private StringParser(String expected) {
        this.expected = expected;
    }

    public static StringParser string(String value) {
        return new StringParser(value);
    }

    @Override
    public Result<String> parse(Sequence<Character> input) {
        Pair<Sequence<Character>, Sequence<Character>> pair = input.splitAt(expected.length());
        String actual = pair.first().toString("");
        if (actual.equals(expected)) {
            return success(expected, pair.second());
        }
        return fail(actual);
    }

    @Override
    public String toString() {
        return expected;
    }
}
