package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Sequence;

import static com.googlecode.totallylazy.Unchecked.cast;

public class DisjunctiveParser<A> extends AbstractParser<A>{
    private final Parser<? extends A> parserA;
    private final Parser<? extends A> parserB;

    private DisjunctiveParser(Parser<? extends A> parserA, Parser<? extends A> parserB) {
        this.parserA = parserA;
        this.parserB = parserB;
    }

    public static <A> DisjunctiveParser<A> or(Parser<? extends A> parserA, Parser<? extends A> parserB) {
        return new DisjunctiveParser<A>(parserA, parserB);
    }

    @Override
    public String toString() {
        return String.format("%s or %s", parserA, parserB);
    }

    @Override
    public Result<A> call(Sequence<Character> characters) throws Exception {
        Result<? extends A> result = parserA.call(characters);
        if(result instanceof Failure){
            return cast(parserB.call(characters));
        }
        return cast(result);
    }
}
