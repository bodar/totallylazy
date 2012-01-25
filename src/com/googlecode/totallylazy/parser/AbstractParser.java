package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Sequence;

public abstract class AbstractParser<A> implements Parser<A> {
    @Override
    public <B> Parser<B> map(Callable1<? super A, ? extends B> callable) {
        return MappingParser.map(this, callable);
    }

    protected Failure<A> fail(A actual) {
        return Failure.failure(String.format("Expected '%s' but '%s'", expected(), actual));
    }

    protected abstract Object expected();

    public A parse(Sequence<Character> characters) throws Exception {
        return call(characters).value();
    }
}
