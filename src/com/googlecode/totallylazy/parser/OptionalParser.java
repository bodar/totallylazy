package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Option;

import java.nio.CharBuffer;

import static com.googlecode.totallylazy.parser.ReturnsParser.returns;
import static java.lang.String.format;

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
        return format("optional(%s)", parserA.toString());
    }

    @Override
    public Result<Option<A>> parse(CharBuffer characters) throws Exception {
        return parserA.map(Option.<A>option()).
                or(returns(Option.<A>none())).
                parse(characters);
    }
}
