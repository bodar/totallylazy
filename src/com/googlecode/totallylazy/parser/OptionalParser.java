package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Segment;

import static com.googlecode.totallylazy.parser.ReturnsParser.returns;

public class OptionalParser<A> extends Parser<Option<A>> {
    private final Parser<? extends A> parserA;

    private OptionalParser(Parse<? extends A> parserA) {
        this.parserA = Parsers.parser(parserA);
    }

    public static <A> OptionalParser<A> optional(Parse<? extends A> parserA) {
        return new OptionalParser<A>(parserA);
    }

    @Override
    public String toString() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Result<Option<A>> parse(Segment<Character> characters) throws Exception {
        return parserA.map(Option.<A>option()).
                or(returns(Option.<A>none())).
                parse(characters);
    }
}
