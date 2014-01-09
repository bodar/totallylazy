package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Returns;
import com.googlecode.totallylazy.Triple;

import java.nio.CharBuffer;
import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.Functions.returns;
import static com.googlecode.totallylazy.Unchecked.cast;
import static com.googlecode.totallylazy.callables.LazyCallable.lazy;

public class TripleParser<A, B, C> extends Parser<Triple<A, B, C>> {
    private final Returns<? extends Parse<? extends A>> parserA;
    private final Returns<? extends Parse<? extends B>> parserB;
    private final Returns<? extends Parse<? extends C>> parserC;

    private TripleParser(Callable<? extends Parse<? extends A>> parserA, Callable<? extends Parse<? extends B>> parserB, Callable<? extends Parse<? extends C>> parserC) {
        this.parserA = lazy(parserA);
        this.parserB = lazy(parserB);
        this.parserC = lazy(parserC);
    }

    public static <A, B, C> TripleParser<A, B, C> tripleOf(final Parse<? extends A> parserA, final Parse<? extends B> parserB, final Parse<? extends C> parserC) {
        return tripleOf(parserA, parserB, returns(parserC));
    }

    public static <A, B, C> TripleParser<A, B, C> tripleOf(final Parse<? extends A> parserA, final Parse<? extends B> parserB, final Callable<? extends Parse<? extends C>> parserC) {
        return tripleOf(parserA, returns(parserB), parserC);
    }

    public static <A, B, C> TripleParser<A, B, C> tripleOf(final Parse<? extends A> parserA, final Callable<? extends Parse<? extends B>> parserB, final Callable<? extends Parse<? extends C>> parserC) {
        return tripleOf(returns(parserA), parserB, parserC);
    }

    public static <A, B, C> TripleParser<A, B, C> tripleOf(final Callable<? extends Parse<? extends A>> parserA, final Callable<? extends Parse<? extends B>> parserB, final Callable<? extends Parse<? extends C>> parserC) {
        return new TripleParser<A, B, C>(parserA, parserB, parserC);
    }

    @Override
    public String toString() {
        return String.format("%s and %s and %s", parserA, parserB, parserC);
    }

    @Override
    public Result<Triple<A, B, C>> parse(CharBuffer characters) throws Exception {
        Result<? extends A> resultA = parserA.value().parse(characters);
        if (resultA instanceof Failure) return cast(resultA);

        Result<? extends B> resultB = parserB.value().parse(resultA.remainder());
        if (resultB instanceof Failure) return cast(resultB);

        Result<? extends C> resultC = parserC.value().parse(resultB.remainder());
        if (resultC instanceof Failure) return cast(resultC);

        return Success.success(Triple.triple(resultA.value(), resultB.value(), resultC.value()), resultC.remainder());
    }
}
