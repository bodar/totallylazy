package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Option;

public abstract class AbstractParser<A> implements Parser<A> {
    protected Failure<A> fail(A actual) {
        return Failure.failure(String.format("Expected '%s' but '%s'", toString(), actual));
    }

    public abstract String toString();

    @Override
    public <B> AbstractParser<B> map(Callable1<? super A, ? extends B> callable) {
        return MappingParser.map(this, callable);
    }

    public AbstractParser<A> or(Parser<? extends A> parser){
        return DisjunctiveParser.or(this, parser);
    }

    public AbstractParser<Option<A>> optional(){
        return OptionalParser.optional(this);
    }
}
