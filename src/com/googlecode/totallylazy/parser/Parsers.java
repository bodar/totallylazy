package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.functions.Function1;
import com.googlecode.totallylazy.functions.Callables;
import com.googlecode.totallylazy.Characters;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.predicates.Predicate;
import com.googlecode.totallylazy.Triple;

import java.io.PrintStream;
import java.util.List;
import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.Sequences.sequence;

public interface Parsers {
    Function1<Iterable<?>, String> toString = iterable -> sequence(iterable).toString("");

    static <A> Parser<A> parser(final Parser<? extends A> parser) {
        return MappingParser.map(parser, Callables.<A>returnArgument());
    }

    // Use in Java 7+, for Java 6 use Parsers.reference
    static <T> Parser<T> lazy(Callable<? extends Parser<T>> value) {
        return LazyParser.lazy(value);
    }

    // Use in Java 6, for Java 7+ use Parsers.lazy
    static <T> ReferenceParser<T> reference(){
        return ReferenceParser.reference();
    }

    static Parser<Character> character(Predicate<Character> value) {
        return CharacterParser.character(value);
    }

    static Parser<Character> character(char value) {
        return CharacterParser.character(value);
    }

    static Parser<CharSequence> characters(Predicate<Character> value) { return CharactersParser.characters(value); }

    static Parser<Character> isChar(Predicate<Character> value) {
        return CharacterParser.character(value);
    }

    static Parser<Character> isChar(char value) {
        return CharacterParser.character(value);
    }

    static Parser<Character> wsChar(char value) {
        return ws(isChar(value));
    }

    static <A> Parser<A> ws(Parser<A> parser) {
        return parser.surroundedBy(isChar(Characters.whitespace).many());
    }

    static Parser<Character> notChar(char value) {
        return CharacterParser.notChar(value);
    }

    static Parser<Character> among(String value) {
        return CharacterParser.character(Characters.among(value));
    }

    static Parser<Character> notAmong(String value) {
        return CharacterParser.character(Characters.notAmong(value));
    }

    static Parser<String> string(String value) {
        return StringParser.string(value);
    }

    static Parser<String> string(Iterable<? extends Predicate<? super Character>> value) {
        return PredicatesParser.string(value);
    }

    @SafeVarargs
    static Parser<String> string(Predicate<? super Character>... predicates) {
        return PredicatesParser.string(predicates);
    }

    /**
     * Use Parser.pretty() or Parsers.pretty() instead
     */
    @Deprecated
    static Parser<String> pattern(String value, String pretty) {
        return pattern(value).pretty(pretty);
    }
    static Parser<String> pattern(String value) {
        return PatternParser.pattern(value);
    }

    static <A> Parser<A> pretty(String pretty, Parser<A> parse) {
        return PrettyParser.pretty(parse, pretty);
    }

    static <A, B> Parser<B> map(Parser<? extends A> source, Function1<? super A, ? extends B> callable) {
        return MappingParser.map(source, callable);
    }

    static <A> Parser<Option<A>> optional(Parser<? extends A> parserA) {
        return OptionalParser.optional(parserA);
    }

    static <A> Parser<A> or(Iterable<? extends Parser<? extends A>> parsers) {
        return OrParser.or(parsers);
    }

    @SafeVarargs
    static <A> Parser<A> or(Parser<? extends A>... parsers) {
        return or(sequence(parsers));
    }

    static <A> Parser<A> returns(A a) {
        return new ReturnsParser<>(a);
    }

    static <T> Parser<T> constant(T value) {
        return returns(value);
    }

    static <A> Parser<List<A>> many(Parser<? extends A> parser) {
        return ManyParser.many(parser);
    }

    static <A> Parser<List<A>> list(final Iterable<? extends Parser<? extends A>> parsers) {
        return ListParser.list(parsers);
    }

    @SafeVarargs
    static <A> Parser<List<A>> list(final Parser<? extends A>... parsers) {
        return list(sequence(parsers));
    }

    static <A, B> Parser<Pair<A, B>> pair(final Parser<? extends A> parserA, final Parser<? extends B> parserB) {
        return PairParser.pair(parserA, parserB);
    }

    static <A, B, C> Parser<Triple<A, B, C>> triple(final Parser<? extends A> parserA, final Parser<? extends B> parserB, final Parser<? extends C> parserC) {
        return TripleParser.triple(parserA, parserB, parserC);
    }

    static <A, B> Parser<Pair<A, B>> tuple(final Parser<? extends A> parserA, final Parser<? extends B> parserB) {
        return PairParser.pair(parserA, parserB);
    }

    static <A, B, C> Parser<Triple<A, B, C>> tuple(final Parser<? extends A> parserA, final Parser<? extends B> parserB, final Parser<? extends C> parserC) {
        return TripleParser.triple(parserA, parserB, parserC);
    }

    static <A> Parser<A> between(Parser<?> before, Parser<A> parserB, Parser<?> after) {
        return TripleParser.triple(before, parserB, after).map(Callables.<A>second());
    }

    static <T> Parser<T> debug(String name, Parser<T> parser) {
        return debug(System.out, name, parser);
    }

    static <T> Parser<T> debug(PrintStream printStream, String name, Parser<T> parser) {
        return new DebugParser<>(parser, name, printStream);
    }
}
