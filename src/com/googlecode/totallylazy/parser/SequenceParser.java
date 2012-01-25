package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Value;

import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.Unchecked.cast;
import static com.googlecode.totallylazy.callables.LazyCallable.lazy;

public class SequenceParser<A,B> extends AbstractParser<Pair<A,B>>{
    private final Parser<? extends A> parserA;
    private final Parser<? extends B> parserB;

    public SequenceParser(Parser<? extends A> parserA, Parser<? extends B> parserB) {
        this.parserA = parserA;
        this.parserB = parserB;
    }

    public static <A, B> SequenceParser<A, B> sequenceOf(final Parser<? extends A> parserA, final Parser<? extends B> parserB) {
        return new SequenceParser<A, B>(parserA, parserB);
    }

    @Override
    public String toString() {
        return String.format("%s and %s", parserA, parserB);
    }

    @Override
    public Result<Pair<A, B>> parse(Sequence<Character> characters) throws Exception {
        Result<? extends A> resultA = parserA.parse(characters);
        if(resultA instanceof Success){
            Success<A> successA = cast(resultA);
            Result<? extends B> resultB = parserB.parse(successA.remainder());
            if(resultB instanceof Success){
                Success<B> successB = cast(resultB);
                return Success.success(Pair.pair(successA.value(), successB.value()), successB.remainder());
            }
            return cast(resultB);
        }
        return cast(resultA);
    }
}
