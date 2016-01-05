package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Segment;
import com.googlecode.totallylazy.Triple;

import static com.googlecode.totallylazy.Unchecked.cast;

class TripleParser<A, B, C> implements Parser<Triple<A, B, C>> {
    private final Parser<? extends A> parserA;
    private final Parser<? extends B> parserB;
    private final Parser<? extends C> parserC;

    private TripleParser(Parser<? extends A> parserA, Parser<? extends B> parserB, Parser<? extends C> parserC) {
        this.parserA = parserA;
        this.parserB = parserB;
        this.parserC = parserC;
    }

    static <A, B, C> TripleParser<A, B, C> triple(final Parser<? extends A> parserA, final Parser<? extends B> parserB, final Parser<? extends C> parserC) {
        return new TripleParser<A, B, C>(parserA, parserB, parserC);
    }

    @Override
    public Result<Triple<A, B, C>> parse(Segment<Character> characters) {
        Result<? extends A> resultA = parserA.parse(characters);
        if (resultA instanceof Failure) return cast(resultA);

        Result<? extends B> resultB = parserB.parse(resultA.remainder());
        if (resultB instanceof Failure) return cast(resultB);

        Result<? extends C> resultC = parserC.parse(resultB.remainder());
        if (resultC instanceof Failure) return cast(resultC);

        return Success.success(Triple.triple(resultA.value(), resultB.value(), resultC.value()), resultC.remainder());
    }

    @Override
    public String toString() {
        return String.format("%s and %s and %s", parserA, parserB, parserC);
    }
}
