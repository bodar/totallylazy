package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callables;
import com.googlecode.totallylazy.Function1;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Segment;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;
import com.googlecode.totallylazy.Strings;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.Strings.UTF8;

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

    public <B> Parser<List<A>> separatedBy(Parse<?> parser){
        return then(OptionalParser.optional(parser)).map(Callables.<A>first()).many();
    }

    public Parser<A> or(Parse<? extends A> parser){
        return OrParser.or(this, parser);
    }

    public Parser<Option<A>> optional(){
        return OptionalParser.optional(this);
    }
    
    public Parser<List<A>> many() {
        return ManyParser.many(this);
    }

    public Result<A> parse(CharSequence value) throws Exception {
        return parse(Segment.constructors.characters(value));
    }

    public Result<A> parse(Reader value) throws Exception {
        return parse(Segment.constructors.characters(value));
    }

    public Result<A> parse(InputStream value) throws Exception {
        return parse(Segment.constructors.characters(new InputStreamReader(value, UTF8)));
    }

    public Parser<Void> ignore() {
        return map(new Function1<A, Void>() {
            @Override
            public Void call(A value) throws Exception {
                return null;
            }
        });
    }

    public Parser<List<A>> times(int number){
        return Parsers.sequenceOf(Sequences.repeat(this).take(number));
    }
}
