package com.googlecode.totallylazy.parser;

import java.nio.CharBuffer;

public class ReturnsParser<A> extends Parser<A> {
    private final A a;

    private ReturnsParser(A a) {
        this.a = a;
    }

    public static <A> ReturnsParser<A> returns(A a) {
        return new ReturnsParser<A>(a);
    }

    @Override
    public String toString() {
        return a.toString();
    }

    @Override
    public Result<A> parse(CharBuffer sequence) throws Exception {
        return Success.success(a, sequence);
    }
}
