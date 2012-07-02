package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Function;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Segment;

import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.Function.returns;
import static com.googlecode.totallylazy.Unchecked.cast;
import static com.googlecode.totallylazy.callables.LazyCallable.lazy;

public class PairParser<A,B> extends AbstractParser<Pair<A,B>> {
    private final Function<? extends Parser<? extends A>> parserA;
    private final Function<? extends Parser<? extends B>> parserB;

    private PairParser(Callable<? extends Parser<? extends A>> parserA, Callable<? extends Parser<? extends B>> parserB) {
        this.parserA = lazy(parserA);
        this.parserB = lazy(parserB);
    }

    public static <A, B> PairParser<A, B> pairOf(final Parser<? extends A> parserA, final Parser<? extends B> parserB) {
        return pairOf(parserA, returns(parserB));
    }

    public static <A, B> PairParser<A, B> pairOf(final Parser<? extends A> parserA, final Callable<? extends Parser<? extends B>> parserB) {
        return pairOf(returns(parserA), parserB);
    }

    public static <A, B> PairParser<A, B> pairOf(final Callable<? extends Parser<? extends A>> parserA, final Callable<? extends Parser<? extends B>> parserB) {
        return new PairParser<A, B>(parserA, parserB);
    }

    @Override
    public String toString() {
        return String.format("%s and %s", parserA, parserB);
    }

    @Override
    public Result<Pair<A, B>> parse(Segment<Character> characters) throws Exception {
        Result<? extends A> resultA = parserA.value().parse(characters);
        if (resultA instanceof Failure) return cast(resultA);
        Success<A> successA = cast(resultA);

        Result<? extends B> resultB = parserB.value().parse(successA.remainder());
        if (resultB instanceof Failure) return cast(resultB);
        Success<B> successB = cast(resultB);

        return Success.success(Pair.pair(successA.value(), successB.value()), successB.remainder());
    }
}
