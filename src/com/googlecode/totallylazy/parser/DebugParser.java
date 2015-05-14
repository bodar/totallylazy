package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Segment;

import java.io.PrintStream;

class DebugParser<T> extends DelegateParser<T> {
    private final String name;
    private final PrintStream printStream;

    DebugParser(Parser<T> parser, String name, PrintStream printStream) {
        super(parser);
        this.name = name;
        this.printStream = printStream;
    }

    @Override
    public Result<T> parse(Segment<Character> characters) {
        Result<T> result = delegate.parse(characters);
        printStream.println(name + " -> " + result);
        return result;
    }
}
