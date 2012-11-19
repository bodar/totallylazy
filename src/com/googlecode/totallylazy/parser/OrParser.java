package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Segment;

import static com.googlecode.totallylazy.Unchecked.cast;

public class OrParser<A> extends Parser<A> {
    private final Parse<? extends A> parserA;
    private final Parse<? extends A> parserB;

    private OrParser(Parse<? extends A> parserA, Parse<? extends A> parserB) {
        this.parserA = parserA;
        this.parserB = parserB;
    }

    public static <A> OrParser<A> or(Parse<? extends A> parserA, Parse<? extends A> parserB) {
        return new OrParser<A>(parserA, parserB);
    }

    @Override
    public String toString() {
        return String.format("%s or %s", parserA, parserB);
    }

    @Override
    public Result<A> parse(Segment<Character> characters) throws Exception {
        Result<? extends A> result = parserA.parse(characters);
        if (result instanceof Failure) {
            return cast(parserB.parse(characters));
        }
        return cast(result);
    }
}
