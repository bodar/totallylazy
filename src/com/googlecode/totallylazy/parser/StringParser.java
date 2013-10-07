package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.collections.PersistentList;

import java.nio.CharBuffer;

import static com.googlecode.totallylazy.collections.PersistentList.constructors.empty;
import static com.googlecode.totallylazy.parser.Success.success;

public class StringParser extends Parser<String> {
    private final CharSequence expected;

    public StringParser(CharSequence expected) {
        this.expected = expected;
    }

    public static StringParser string(CharSequence value) {
        return new StringParser(value);
    }

    @Override
    public Result<String> parse(CharBuffer buffer) throws Exception {
        if (buffer.remaining() < expected.length()) return fail(expected, buffer.toString());
        buffer.mark();
        PersistentList<Character> accumulator = empty();
        for (int i = 0; i < expected.length(); i++) {
            char e = expected.charAt(i);
            char a = buffer.get();
            accumulator = accumulator.cons(a);
            if (e != a) {
                buffer.reset();
                return fail(expected, accumulator.reverse().toSequence().toString(""));
            }
        }
        return success(expected.toString(), buffer);
    }

    @Override
    public String toString() {
        return expected.toString();
    }
}