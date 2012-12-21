package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callables;
import com.googlecode.totallylazy.Function1;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Segment;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Triple;
import com.googlecode.totallylazy.regex.Regex;

import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.Characters.identifierPart;
import static com.googlecode.totallylazy.Characters.identifierStart;

public class Parsers {
    private static Function1<Pair<Character, ? extends Segment<Character>>, String> toString = new Function1<Pair<Character, ? extends Segment<Character>>, String>() {
        @Override
        public String call(Pair<Character, ? extends Segment<Character>> pair) throws Exception {
            return pair.first() + Segment.methods.toString(pair.second(), "");
        }
    };
    public static Parser<String> identifier = character(identifierStart).then(character(identifierPart).many()).map(toString);

    public static <A> Parser<A> parser(final Parse<? extends A> parser) {
        return MappingParser.map(parser, Callables.<A>returnArgument());
    }

    public static Parser<Character> character(Predicate<Character> value) {
        return CharacterParser.character(value);
    }

    public static Parser<Character> character(char value) {
        return CharacterParser.character(value);
    }

    public static Parser<Character> isChar(Predicate<Character> value) {
        return CharacterParser.character(value);
    }

    public static Parser<Character> isChar(char value) {
        return CharacterParser.character(value);
    }

    public static Parser<Character> notChar(char value) {
        return CharacterParser.notChar(value);
    }

    public static Parser<String> string(CharSequence value) {
        return StringParser.string(value);
    }

    public static Parser<String> string(Iterable<? extends Parse<Character>> map) {return StringParser.string(map);}

    public static Parser<String> string(final Parse<Character> a, final Parse<Character> b) {
        return StringParser.string(a, b);
    }

    public static Parser<String> string(final Parse<Character> a, final Parse<Character> b, final Parse<Character> c) {
        return StringParser.string(a, b, c);
    }

    public static Parser<String> string(final Parse<Character> a, final Parse<Character> b, final Parse<Character> c, final Parse<Character> d) {
        return StringParser.string(a, b, c, d);
    }

    public static Parser<String> string(final Parse<Character> a, final Parse<Character> b, final Parse<Character> c, final Parse<Character> d, final Parse<Character> e) {
        return StringParser.string(a, b, c, d, e);
    }

    public static Parser<String> string(final Parse<Character>... parsers) {
        return StringParser.string(parsers);
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

    public static <A> Parser<A> or(Parse<? extends A> parserA, Parse<? extends A> parserB) {
        return OrParser.or(parserA, parserB);
    }

    public static <A, B> Parser<Pair<A, B>> pairOf(final Parse<? extends A> parserA, final Parse<? extends B> parserB) {
        return PairParser.pairOf(parserA, parserB);
    }

    public static <A, B> Parser<Pair<A, B>> pairOf(final Parse<? extends A> parserA, final Callable<? extends Parse<? extends B>> parserB) {
        return PairParser.pairOf(parserA, parserB);
    }

    public static <A, B> Parser<Pair<A, B>> pairOf(final Callable<? extends Parse<? extends A>> parserA, final Callable<? extends Parse<? extends B>> parserB) {
        return PairParser.pairOf(parserA, parserB);
    }

    public static <A> Parser<A> returns(A a) {
        return ReturnsParser.returns(a);
    }

    public static <A> Parser<Sequence<A>> many(Parse<? extends A> parser) {
        return ManyParser.many(parser);
    }

    public static <A> Parser<Sequence<A>> sequenceOf(final Sequence<? extends Parse<? extends A>> parsers) {
        return SequenceParser.sequenceOf(parsers);
    }

    public static <A> Parser<Sequence<A>> sequenceOf(final Parse<? extends A> a, final Parse<? extends A> b) {
        return SequenceParser.sequenceOf(a, b);
    }

    public static <A> Parser<Sequence<A>> sequenceOf(final Parse<? extends A> a, final Parse<? extends A> b, final Parse<? extends A> c) {
        return SequenceParser.sequenceOf(a, b, c);
    }

    public static <A> Parser<Sequence<A>> sequenceOf(final Parse<? extends A> a, final Parse<? extends A> b, final Parse<? extends A> c, final Parse<? extends A> d) {
        return SequenceParser.sequenceOf(a, b, c, d);
    }

    public static <A> Parser<Sequence<A>> sequenceOf(final Parse<? extends A> a, final Parse<? extends A> b, final Parse<? extends A> c, final Parse<? extends A> d, final Parse<? extends A> e) {
        return SequenceParser.sequenceOf(a, b, c, d, e);
    }

    public static <A> Parser<Sequence<A>> sequenceOf(final Parse<? extends A>... parsers) {
        return SequenceParser.sequenceOf(parsers);
    }

    public static <A, B, C> Parser<Triple<A, B, C>> tripleOf(final Parse<? extends A> parserA, final Parse<? extends B> parserB, final Parse<? extends C> parserC) {
        return TripleParser.tripleOf(parserA, parserB, parserC);
    }

    public static <A, B, C> Parser<Triple<A, B, C>> tripleOf(final Parse<? extends A> parserA, final Parse<? extends B> parserB, final Callable<? extends Parse<? extends C>> parserC) {
        return TripleParser.tripleOf(parserA, parserB, parserC);
    }

    public static <A, B, C> Parser<Triple<A, B, C>> tripleOf(final Parse<? extends A> parserA, final Callable<? extends Parse<? extends B>> parserB, final Callable<? extends Parse<? extends C>> parserC) {
        return TripleParser.tripleOf(parserA, parserB, parserC);
    }

    public static <A, B, C> Parser<Triple<A, B, C>> tripleOf(final Callable<? extends Parse<? extends A>> parserA, final Callable<? extends Parse<? extends B>> parserB, final Callable<? extends Parse<? extends C>> parserC) {
        return TripleParser.tripleOf(parserA, parserB, parserC);
    }
}
