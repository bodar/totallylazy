package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callables;
import com.googlecode.totallylazy.Characters;
import com.googlecode.totallylazy.Function1;
import com.googlecode.totallylazy.Lazy;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;
import com.googlecode.totallylazy.Triple;
import com.googlecode.totallylazy.regex.Regex;

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

//    public static Parser<String> identifier =
//            character(identifierStart).
//            then(character(identifierPart).many()).
//                    map(new Function1<Pair<Character, List<Character>>, List<Character>>() {
//                        @Override
//                        public List<Character> call(Pair<Character, List<Character>> p) throws Exception {
//                            List<Character> list = p.second();
//                            list.;
//                            return list;
//                        }
//                    }).map(toString);

    public static <A> Parser<A> parser(final Parse<? extends A> parser) {
        return MappingParser.map(parser, Callables.<A>returnArgument());
    }

    public static <T> Parser<T> lazy(Callable<? extends Parse<T>> value) {
        return LazyParser.lazy(value);
    }

    public static <T> ReferenceParser<T> reference(){
        return ReferenceParser.reference();
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

    public static Parser<String> string(Predicate<? super Character> first) {
        return PredicatesParser.string(sequence(first));
    }

    public static Parser<String> string(Predicate<? super Character> first, Predicate<? super Character> second) {
        return PredicatesParser.string(Sequences.<Predicate<? super Character>>sequence(first, second));
    }

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

    public static <A> Parser<List<A>> many(Parse<? extends A> parser) {
        return ManyParser.many(parser);
    }

    public static <A> Parser<List<A>> list(final Iterable<? extends Parse<? extends A>> parsers) {
        return ListParser.list(parsers);
    }

    public static <A> Parser<List<A>> list(final Parse<? extends A> a, final Parse<? extends A> b) {
        return list(sequence(a, b));
    }

    public static <A> Parser<List<A>> list(final Parse<? extends A> a, final Parse<? extends A> b, final Parse<? extends A> c) {
        return list(sequence(a, b, c));
    }

    public static <A> Parser<List<A>> list(final Parse<? extends A> a, final Parse<? extends A> b, final Parse<? extends A> c, final Parse<? extends A> d) {
        return list(sequence(a, b, c, d));
    }

    public static <A> Parser<List<A>> list(final Parse<? extends A> a, final Parse<? extends A> b, final Parse<? extends A> c, final Parse<? extends A> d, final Parse<? extends A> e) {
        return list(sequence(a, b, c, d, e));
    }

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
}
