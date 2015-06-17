package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Binary;
import com.googlecode.totallylazy.Function1;
import com.googlecode.totallylazy.Callables;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;
import com.googlecode.totallylazy.Unary;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import static com.googlecode.totallylazy.Closeables.using;
import static com.googlecode.totallylazy.Functions.function;
import static com.googlecode.totallylazy.Segment.constructors.characters;
import static com.googlecode.totallylazy.Sequences.foldLeft;
import static com.googlecode.totallylazy.Sequences.foldRight;
import static com.googlecode.totallylazy.Sequences.join;
import static com.googlecode.totallylazy.Strings.UTF8;
import static com.googlecode.totallylazy.callables.Compose.compose;

public abstract class Parser<A> implements Parse<A> {
    protected Failure<A> fail(Object expected, Object actual) {
        return Failure.failure(expected, actual);
    }

    public abstract String toString();

    @Override
    public <B> Parser<B> map(Function1<? super A, ? extends B> callable) {
        return MappingParser.map(this, callable);
    }

    public <B> Parser<Pair<A, B>> then(Parse<? extends B> parser) {
        return PairParser.pair(this, parser);
    }

    public <B> Parser<B> next(Parse<? extends B> parser) {
        return then(parser).map(Callables.<B>second());
    }

    public Parser<A> followedBy(Parse<?> parser) {
        return then(parser).map(Callables.<A>first());
    }

    public Parser<A> between(Parse<?> before, Parse<?> after) {
        return Parsers.between(before, this, after);
    }

    public Parser<A> surroundedBy(Parse<?> parser) {
        return between(parser, parser);
    }

    public Parser<List<A>> sepBy(Parse<?> parser) {
        return separatedBy(parser);
    }

    public Parser<List<A>> sepBy1(Parse<?> parser) {
        return sep(parser).many(1);
    }

    public Parser<List<A>> separatedBy(Parse<?> parser) {
        return sep(parser).many();
    }

    private Parser<A> sep(Parse<?> parser) {return then(OptionalParser.optional(parser)).map(Callables.<A>first());}

    public Parser<Sequence<A>> seqBy(Parse<?> parser) {
        return sequencedBy(parser);
    }

    public Parser<Sequence<A>> sequencedBy(Parse<?> parser) {
        return sep(parser).sequence();
    }

    public Parser<A> or(Parse<? extends A> parser) {
        return Parsers.or(this, parser);
    }

    public Parser<Option<A>> optional() {
        return OptionalParser.optional(this);
    }

    public Result<A> parse(CharSequence value) {
        return parse(characters(value));
    }

    public Result<A> parse(Reader value) {
        return parse(characters(value));
    }

    public Result<A> parse(InputStream value) {
        return parse(characters(new InputStreamReader(value, UTF8)));
    }

    public Parser<Void> ignore() {
        return map(value -> null);
    }

    public Parser<List<A>> times(int number) {
        return Parsers.list(Sequences.repeat(this).take(number));
    }

    public Parser<List<A>> many() {
        return ManyParser.many(this);
    }

    public Parser<Sequence<A>> sequence() {
        return SequenceParser.sequence(this);
    }

    public Parser<List<A>> many1() {
        return many(1);
    }

    public Parser<List<A>> many(int min) {
        return times(min).then(many()).map(p -> join(p.first(), p.second()).toList());
    }

    public Parser<A> debug(String name) {
        return Parsers.debug(name, this);
    }

    public <R> Parser<R> returns(R value) {
        return next(Parsers.returns(value));
    }

    public Parser<A> peek() {
        return new PeekParser<>(this);
    }

    public Parser<A> peek(Parser<A> parser) {
        return followedBy(parser.peek());
    }

    public Parser<A> infixLeft(Parser<? extends Binary<A>> op){
        return then(op.then(this).many()).map(pair ->
                foldLeft(pair.second(), pair.first(), (a, p) -> p.first().call(a, p.second())));
    }

    public Parser<A> infixRight(Parser<? extends Binary<A>> op){
        return then(op).many().then(this).map(pair ->
                foldRight(pair.first(), pair.second(), (p, a) -> p.second().call(p.first(), a)));
    }

    public Parser<A> prefix(Parser<? extends Unary<A>> op){
        return op.many().then(this).map(pair ->
                foldLeft(pair.first(), pair.second(), (a, p) -> p.call(a) ));
    }
}
