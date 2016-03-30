package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Functor;
import com.googlecode.totallylazy.Segment;
import com.googlecode.totallylazy.functions.Binary;
import com.googlecode.totallylazy.functions.Callables;
import com.googlecode.totallylazy.functions.Function1;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;
import com.googlecode.totallylazy.functions.Unary;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import static com.googlecode.totallylazy.Segment.constructors.characters;
import static com.googlecode.totallylazy.Sequences.foldLeft;
import static com.googlecode.totallylazy.Sequences.foldRight;
import static com.googlecode.totallylazy.Sequences.join;
import static com.googlecode.totallylazy.Strings.UTF8;

public interface Parser<A> extends Functor<A> {
    Result<A> parse(Segment<Character> characters);

    default Failure<A> fail(Object expected, Object actual) {
        return Failure.failure(expected, actual);
    }

    @Override String toString();

    @Override
    default <B> Parser<B> map(Function1<? super A, ? extends B> callable) {
        return MappingParser.map(this, callable);
    }

    default <B> Parser<B> flatMap(Function1<? super A, ? extends Result<B>> callable) {
        return FlatMappingParser.flatMap(this, callable);
    }

    default <B> Parser<Pair<A, B>> then(Parser<? extends B> parser) {
        return PairParser.pair(this, parser);
    }

    default <B> Parser<B> next(Parser<? extends B> parser) {
        return then(parser).map(Callables.<B>second());
    }

    default Parser<A> followedBy(Parser<?> parser) {
        return then(parser).map(Callables.<A>first());
    }

    default Parser<A> between(Parser<?> before, Parser<?> after) {
        return Parsers.between(before, this, after);
    }

    default Parser<A> surroundedBy(Parser<?> parser) {
        return between(parser, parser);
    }

    default Parser<List<A>> sepBy(Parser<?> parser) {
        return separatedBy(parser);
    }

    default Parser<List<A>> sepBy1(Parser<?> parser) {
        return followedByOption(parser).many(1);
    }

    default Parser<List<A>> separatedBy(Parser<?> parser) {
        return followedByOption(parser).many();
    }

    default Parser<A> followedByOption(Parser<?> parser) {return followedBy(OptionalParser.optional(parser));}

    default Parser<Sequence<A>> seqBy(Parser<?> parser) {
        return sequencedBy(parser);
    }

    default Parser<Sequence<A>> sequencedBy(Parser<?> parser) {
        return followedByOption(parser).sequence();
    }

    default Parser<A> or(Parser<? extends A> parser) {
        return Parsers.or(this, parser);
    }

    default Parser<Option<A>> optional() {
        return OptionalParser.optional(this);
    }

    default Result<A> parse(CharSequence value) {
        return parse(characters(value));
    }

    default Result<A> parse(Reader value) {
        return parse(characters(value));
    }

    default Result<A> parse(InputStream value) {
        return parse(characters(new InputStreamReader(value, UTF8)));
    }

    default Parser<Void> ignore() {
        return map(value -> null);
    }

    default Parser<List<A>> times(int number) {
        return Parsers.list(Sequences.repeat(this).take(number));
    }

    default Parser<List<A>> many() {
        return ManyParser.many(this);
    }

    default Parser<Sequence<A>> sequence() {
        return SequenceParser.sequence(this);
    }

    default Parser<A> pretty(String pretty) {
        return Parsers.pretty(pretty, this);
    }

    default Parser<List<A>> many1() {
        return many(1);
    }

    default Parser<List<A>> many(int min) {
        return times(min).then(many()).map(p -> join(p.first(), p.second()).toList());
    }

    default Parser<A> debug(String name) {
        return Parsers.debug(name, this);
    }

    default <R> Parser<R> returns(R value) {
        return next(Parsers.returns(value));
    }

    default Parser<A> peek() {
        return new PeekParser<>(this);
    }

    default Parser<A> peek(Parser<A> parser) {
        return followedBy(parser.peek());
    }

    default Parser<A> infixLeft(Parser<? extends Binary<A>> op){
        return then(op.then(this).many()).map(pair ->
                foldLeft(pair.second(), pair.first(), (a, p) -> p.first().call(a, p.second())));
    }

    default Parser<A> infixRight(Parser<? extends Binary<A>> op){
        return then(op).many().then(this).map(pair ->
                foldRight(pair.first(), pair.second(), (p, a) -> p.second().call(p.first(), a)));
    }

    default Parser<A> prefix(Parser<? extends Unary<A>> op){
        return op.many().then(this).map(pair ->
                foldLeft(pair.first(), pair.second(), (a, p) -> p.call(a) ));
    }
}
