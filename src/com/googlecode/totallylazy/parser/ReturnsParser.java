package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Segment;

class ReturnsParser<A> extends Parser<A> {
    private final A a;

    ReturnsParser(A a) {
        this.a = a;
    }

    @Override
    public Result<A> parse(Segment<Character> characters) {
        return Success.success(a, characters);
    }

    @Override
    public String toString() {
        return a.toString();
    }
}