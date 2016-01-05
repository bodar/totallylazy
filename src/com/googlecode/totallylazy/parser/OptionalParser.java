package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Segment;

import static com.googlecode.totallylazy.parser.Success.success;
import static java.lang.String.format;

class OptionalParser<A> implements Parser<Option<A>> {
    private final Parser<? extends A> parserA;

    private OptionalParser(Parser<? extends A> parserA) {
        this.parserA = Parsers.parser(parserA);
    }

    static <A> OptionalParser<A> optional(Parser<? extends A> parserA) {
        return new OptionalParser<A>(parserA);
    }

    @Override
    public String toString() {
        return format("optional(%s)", parserA.toString());
    }

    @Override
    public Result<Option<A>> parse(Segment<Character> characters) {
        Result<? extends A> result = parserA.parse(characters);
        if (result instanceof Failure) return success(Option.<A>none(), characters);
        return success(Option.option(result.value()), result.remainder());
    }
}
