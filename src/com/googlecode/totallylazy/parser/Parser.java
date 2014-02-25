package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callables;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Segment;

import java.util.concurrent.Callable;

public abstract class Parser<A> implements Parse<A> {
    protected Failure<A> fail() {
        return Failure.failure(String.format("%s expected.", toString()));
    }

    protected Failure<A> fail(Object expected, Object actual) {
        return Failure.failure(String.format("%s expected, %s encountered.", expected, actual));
    }

    public abstract String toString();

    @Override
    public <B> Parser<B> map(Callable1<? super A, ? extends B> callable) {
        return MappingParser.map(this, callable);
    }

    public <B> Parser<Pair<A, B>> then(Parse<? extends B> parser){
        return PairParser.pairOf(this, parser);
    }

    public <B> Parser<Pair<A, B>> then(Callable<? extends Parse<? extends B>> parser){
        return PairParser.pairOf(this, parser);
    }

    public <B> Parser<B> next(Parse<? extends B> parser){
        return then(parser).map(Callables.<B>second());
    }

    public <B> Parser<A> followedBy(Parse<?> parser){
        return then(parser).map(Callables.<A>first());
    }

    public <B> Parser<A> between(Parse<?> before, Parse<?> after){
        return TripleParser.tripleOf(before, this, after).map(Callables.<A>second());
    }

    public <B> Parser<A> surroundedBy(Parse<?> parser){
        return between(parser, parser);
    }

    public <B> Parser<Segment<A>> separatedBy(Parse<?> parser){
        return then(OptionalParser.optional(parser)).map(Callables.<A>first()).many();
    }

    public Parser<A> or(Parse<? extends A> parser){
        return OrParser.or(this, parser);
    }

    public Parser<Option<A>> optional(){
        return OptionalParser.optional(this);
    }
    
    public Parser<Segment<A>> many() {
        return ManyParser.many(this);
    }
}
