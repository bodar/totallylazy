package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Segment;

import static com.googlecode.totallylazy.Unchecked.cast;
import static com.googlecode.totallylazy.parser.Success.success;

class PairParser<A, B> extends Parser<Pair<A, B>> {
    private final Parse<? extends A> parserA;
    private final Parse<? extends B> parserB;

    private PairParser(Parse<? extends A> parserA, Parse<? extends B> parserB) {
        this.parserA = parserA;
        this.parserB = parserB;
    }

    static <A, B> PairParser<A, B> pair(final Parse<? extends A> parserA, final Parse<? extends B> parserB) {
        return new PairParser<A, B>(parserA, parserB);
    }

    @Override
    public Result<Pair<A, B>> parse(Segment<Character> characters) {
        Result<? extends A> resultA = parserA.parse(characters);
        if (resultA instanceof Failure) return cast(resultA);

        Result<? extends B> resultB = parserB.parse(resultA.remainder());
        if (resultB instanceof Failure) return cast(resultB);

        return success(Pair.pair(resultA.value(), resultB.value()), resultB.remainder());
    }

    @Override
    public String toString() {
        return String.format("%s and %s", parserA, parserB);
    }
}
