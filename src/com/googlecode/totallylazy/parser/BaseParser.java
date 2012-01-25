package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callables;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Pair;

public abstract class BaseParser<A> implements Parser<A> {
    protected Failure<A> fail(A actual) {
        return Failure.failure(String.format("Expected '%s' but '%s'", toString(), actual));
    }

    public abstract String toString();
    
    public static <A> BaseParser<A> parser(final Parser<? extends A> parser){
        return MappingParser.map(parser, Callables.<A>returnArgument());
    }

    @Override
    public <B> BaseParser<B> map(Callable1<? super A, ? extends B> callable) {
        return MappingParser.map(this, callable);
    }

    public BaseParser<Pair<A, A>> then(Parser<? extends A> parser){
        return SequenceParser.sequenceOf(this, parser);
    }

    public BaseParser<A> or(Parser<? extends A> parser){
        return DisjunctiveParser.or(this, parser);
    }

    public BaseParser<Option<A>> optional(){
        return OptionalParser.optional(this);
    }
}
