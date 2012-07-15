package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callables;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Segment;

import java.util.concurrent.Callable;

public abstract class AbstractParser<A> implements Parser<A> {
    protected Failure<A> fail() {
        return Failure.failure(String.format("'%s' expected", toString()));
    }

    protected Failure<A> fail(Object expected, Object actual) {
        return Failure.failure(String.format("'%s' expected '%s' actual", expected, actual));
    }

    public abstract String toString();
    
    public static <A> AbstractParser<A> parser(final Parser<? extends A> parser){
        return MappingParser.map(parser, Callables.<A>returnArgument());
    }

    @Override
    public <B> AbstractParser<B> map(Callable1<? super A, ? extends B> callable) {
        return MappingParser.map(this, callable);
    }

    public <B> AbstractParser<Pair<A, B>> then(Parser<? extends B> parser){
        return PairParser.pairOf(this, parser);
    }

    public <B> AbstractParser<Pair<A, B>> then(Callable<? extends Parser<? extends B>> parser){
        return PairParser.pairOf(this, parser);
    }

    public <B> AbstractParser<B> next(Parser<? extends B> parser){
        return then(parser).map(Callables.<B>second());
    }

    public <B> AbstractParser<A> followedBy(Parser<?> parser){
        return then(parser).map(Callables.<A>first());
    }

    public <B> AbstractParser<A> between(Parser<?> before, Parser<?> after){
        return TripleParser.tripleOf(before, this, after).map(Callables.<A>second());
    }

    public <B> AbstractParser<A> surroundedBy(Parser<?> parser){
        return between(parser, parser);
    }

    public <B> AbstractParser<Segment<A>> separatedBy(Parser<?> parser){
        return then(OptionalParser.optional(parser)).map(Callables.<A>first()).many();
    }

    public AbstractParser<A> or(Parser<? extends A> parser){
        return OrParser.or(this, parser);
    }

    public AbstractParser<Option<A>> optional(){
        return OptionalParser.optional(this);
    }
    
    public AbstractParser<Segment<A>> many() {
        return ManyParser.many(this);
    }

}
