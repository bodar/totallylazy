package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Segment;

abstract class DelegateParser<A> implements Parser<A> {
    protected final Parser<A> delegate;

    protected DelegateParser(Parser<A> delegate) {
        this.delegate = delegate;
    }

    @Override
    public String toString() {
        return delegate.toString();
    }

    @Override
    public Result<A> parse(Segment<Character> characters) {
        return delegate.parse(characters);
    }
}
