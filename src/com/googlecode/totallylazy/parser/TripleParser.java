package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Function;
import com.googlecode.totallylazy.Segment;
import com.googlecode.totallylazy.Triple;

import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.Functions.returns;
import static com.googlecode.totallylazy.Unchecked.cast;
import static com.googlecode.totallylazy.callables.LazyCallable.lazy;

public class TripleParser<A, B, C> extends AbstractParser<Triple<A, B, C>> {
    private final Function<? extends Parser<? extends A>> parserA;
    private final Function<? extends Parser<? extends B>> parserB;
    private final Function<? extends Parser<? extends C>> parserC;

    private TripleParser(Callable<? extends Parser<? extends A>> parserA, Callable<? extends Parser<? extends B>> parserB, Callable<? extends Parser<? extends C>> parserC) {
        this.parserA = lazy(parserA);
        this.parserB = lazy(parserB);
        this.parserC = lazy(parserC);
    }

    public static <A, B, C> TripleParser<A, B, C> tripleOf(final Parser<? extends A> parserA, final Parser<? extends B> parserB, final Parser<? extends C> parserC) {
        return tripleOf(parserA, parserB, returns(parserC));
    }

    public static <A, B, C> TripleParser<A, B, C> tripleOf(final Parser<? extends A> parserA, final Parser<? extends B> parserB, final Callable<? extends Parser<? extends C>> parserC) {
        return tripleOf(parserA, returns(parserB), parserC);
    }

    public static <A, B, C> TripleParser<A, B, C> tripleOf(final Parser<? extends A> parserA, final Callable<? extends Parser<? extends B>> parserB, final Callable<? extends Parser<? extends C>> parserC) {
        return tripleOf(returns(parserA), parserB, parserC);
    }

    public static <A, B, C> TripleParser<A, B, C> tripleOf(final Callable<? extends Parser<? extends A>> parserA, final Callable<? extends Parser<? extends B>> parserB, final Callable<? extends Parser<? extends C>> parserC) {
        return new TripleParser<A, B, C>(parserA, parserB, parserC);
    }

    @Override
    public String toString() {
        return String.format("%s and %s and %s", parserA, parserB, parserC);
    }

    @Override
    public Result<Triple<A, B, C>> parse(Segment<Character> characters) throws Exception {
        Result<? extends A> resultA = parserA.value().parse(characters);
        if (resultA instanceof Failure) return cast(resultA);
        Success<A> successA = cast(resultA);

        Result<? extends B> resultB = parserB.value().parse(successA.remainder());
        if (resultB instanceof Failure) return cast(resultB);
        Success<B> successB = cast(resultB);

        Result<? extends C> resultC = parserC.value().parse(successB.remainder());
        if (resultC instanceof Failure) return cast(resultC);
        Success<C> successC = cast(resultC);

        return Success.success(Triple.triple(successA.value(), successB.value(), successC.value()), successC.remainder());
    }
}
