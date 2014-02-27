package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Segment;

import static com.googlecode.totallylazy.parser.Success.success;

class StringParser extends Parser<String> {
    private final String expected;

    private StringParser(String expected) {
        this.expected = expected;
    }

    static StringParser string(String expected) {
        return new StringParser(expected);
    }

    @Override
    public Result<String> parse(Segment<Character> characters) {
        Segment<Character> segment = characters;
        StringBuilder result = new StringBuilder();
        for (int i = 0, n = expected.length(); i < n; i++) {
            char e = expected.charAt(i);
            char a = segment.head();
            result.append(a);
            if (e != a) return fail(expected, result.toString());
            segment = segment.tail();
        }
        return success(result.toString(), segment);
    }

    @Override
    public String toString() {
        return expected;
    }
}
