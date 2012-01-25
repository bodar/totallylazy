package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Sequence;

import static com.googlecode.totallylazy.Unchecked.cast;
import static com.googlecode.totallylazy.parser.ReturnsParser.returns;
import static com.googlecode.totallylazy.parser.Success.success;

public class OptionalParser<A> extends BaseParser<Option<A>> {
    private final BaseParser<? extends A> parserA;

    private OptionalParser(Parser<? extends A> parserA) {
        this.parserA = parser(parserA);
    }

    public static <A> OptionalParser<A> optional(Parser<? extends A> parserA) {
        return new OptionalParser<A>(parserA);
    }

    @Override
    public String toString() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Result<Option<A>> parse(Sequence<Character> characters) throws Exception {
        return parserA.map(Option.<A>option()).
                or(returns(Option.<A>none())).
                parse(characters);
    }
}
