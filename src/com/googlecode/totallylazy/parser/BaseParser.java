package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callables;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Sequence;

import java.util.concurrent.Callable;

public abstract class BaseParser<A> implements Parser<A> {
    protected Failure<A> fail() {
        return Failure.failure(String.format("%s expected", toString()));
    }

    public abstract String toString();
    
    public static <A> BaseParser<A> parser(final Parser<? extends A> parser){
        return MappingParser.map(parser, Callables.<A>returnArgument());
    }

    @Override
    public <B> BaseParser<B> map(Callable1<? super A, ? extends B> callable) {
        return MappingParser.map(this, callable);
    }

    public <B> BaseParser<Pair<A, B>> then(Parser<? extends B> parser){
        return PairParser.pairOf(this, parser);
    }

    public <B> BaseParser<Pair<A, B>> then(Callable<? extends Parser<? extends B>> parser){
        return PairParser.pairOf(this, parser);
    }

    public BaseParser<A> or(Parser<? extends A> parser){
        return OrParser.or(this, parser);
    }

    public BaseParser<Option<A>> optional(){
        return OptionalParser.optional(this);
    }
    
    public BaseParser<Sequence<A>> many() {
        return ManyParser.many(this);
    }
}
