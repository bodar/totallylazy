package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callables;
import com.googlecode.totallylazy.Characters;
import com.googlecode.totallylazy.Function1;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Triple;
import com.googlecode.totallylazy.regex.Regex;

import java.io.PrintStream;
import java.util.List;
import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.Sequences.sequence;

public class Parsers {
    public static Function1<Iterable<?>, String> toString = new Function1<Iterable<?>, String>() {
        @Override
        public String call(Iterable<?> iterable) throws Exception {
            return sequence(iterable).toString("");
        }
    };

    public static <A> Parser<A> parser(final Parse<? extends A> parser) {
        return MappingParser.map(parser, Callables.<A>returnArgument());
    }

    // Use in Java 7+, for Java 6 use Parsers.reference
    public static <T> Parser<T> lazy(Callable<? extends Parse<T>> value) {
        return LazyParser.lazy(value);
    }

    // Use in Java 6, for Java 7+ use Parsers.lazy
    public static <T> ReferenceParser<T> reference(){
        return ReferenceParser.reference();
    }

    public static Parser<Character> character(Predicate<Character> value) {
        return CharacterParser.character(value);
    }

    public static Parser<Character> character(char value) {
        return CharacterParser.character(value);
    }

    public static Parser<String> characters(Predicate<Character> value) { return CharactersParser.characters(value); }

    public static Parser<Character> isChar(Predicate<Character> value) {
        return CharacterParser.character(value);
    }

    public static Parser<Character> isChar(char value) {
        return CharacterParser.character(value);
    }

    public static Parser<Character> wsChar(char value) {
        return ws(isChar(value));
    }

    public static <A> Parser<A> ws(Parser<A> parser) {
        return parser.surroundedBy(isChar(Characters.whitespace).many());
    }

    public static Parser<Character> notChar(char value) {
        return CharacterParser.notChar(value);
    }

    public static Parser<Character> among(String value) {
        return CharacterParser.character(Characters.among(value));
    }

    public static Parser<Character> notAmong(String value) {
        return CharacterParser.character(Characters.notAmong(value));
    }

    public static Parser<String> string(String value) {
        return StringParser.string(value);
    }

    public static Parser<String> string(Iterable<? extends Predicate<? super Character>> value) {
        return PredicatesParser.string(value);
    }

    @SafeVarargs
    public static Parser<String> string(Predicate<? super Character>... predicates) {
        return PredicatesParser.string(predicates);
    }

    public static Parser<String> pattern(Regex regex) {
        return PatternParser.pattern(regex);
    }

    public static Parser<String> pattern(String value) {
        return PatternParser.pattern(value);
    }

    public static <A, B> Parser<B> map(Parse<? extends A> source, Callable1<? super A, ? extends B> callable) {
        return MappingParser.map(source, callable);
    }

    public static <A> Parser<Option<A>> optional(Parse<? extends A> parserA) {
        return OptionalParser.optional(parserA);
    }

    public static <A> Parser<A> or(Sequence<? extends Parse<? extends A>> parsers) {
        return OrParser.or(parsers);
    }

    public static <A> Parser<A> or(Parse<? extends A>... parsers) {
        return or(sequence(parsers));
    }

    public static <A> Parser<A> or(Parse<? extends A> a, Parse<? extends A> b) {
        return or(sequence(a, b));
    }

    public static <A> Parser<A> or(Parse<? extends A> a, Parse<? extends A> b, Parse<? extends A> c) {
        return or(sequence(a, b, c));
    }

    public static <A> Parser<A> or(Parse<? extends A> a, Parse<? extends A> b, Parse<? extends A> c, Parse<? extends A> d) {
        return or(sequence(a, b, c, d));
    }

    public static <A> Parser<A> returns(A a) {
        return ReturnsParser.returns(a);
    }

    public static <T> Parser<T> constant(T value) {
        return returns(value);
    }

    public static <A> Parser<List<A>> many(Parse<? extends A> parser) {
        return ManyParser.many(parser);
    }

    public static <A> Parser<List<A>> list(final Iterable<? extends Parse<? extends A>> parsers) {
        return ListParser.list(parsers);
    }

    @SafeVarargs
    public static <A> Parser<List<A>> list(final Parse<? extends A>... parsers) {
        return list(sequence(parsers));
    }

    public static <A, B> Parser<Pair<A, B>> pair(final Parse<? extends A> parserA, final Parse<? extends B> parserB) {
        return PairParser.pair(parserA, parserB);
    }

    public static <A, B, C> Parser<Triple<A, B, C>> triple(final Parse<? extends A> parserA, final Parse<? extends B> parserB, final Parse<? extends C> parserC) {
        return TripleParser.triple(parserA, parserB, parserC);
    }

    public static <A, B> Parser<Pair<A, B>> tuple(final Parse<? extends A> parserA, final Parse<? extends B> parserB) {
        return PairParser.pair(parserA, parserB);
    }

    public static <A, B, C> Parser<Triple<A, B, C>> tuple(final Parse<? extends A> parserA, final Parse<? extends B> parserB, final Parse<? extends C> parserC) {
        return TripleParser.triple(parserA, parserB, parserC);
    }

    public static <A> Parser<A> between(Parse<?> before, Parser<A> parserB, Parse<?> after) {
        return TripleParser.triple(before, parserB, after).map(Callables.<A>second());
    }

    public static <T> Parser<T> debug(String name, Parser<T> parser) {
        return debug(System.out, name, parser);
    }

    public static <T> Parser<T> debug(PrintStream printStream, String name, Parser<T> parser) {
        return new DebugParser<>(parser, name, printStream);
    }
}
