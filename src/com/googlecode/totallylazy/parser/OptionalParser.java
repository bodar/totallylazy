package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Sequence;

import static com.googlecode.totallylazy.Unchecked.cast;
import static com.googlecode.totallylazy.parser.Success.success;

public class OptionalParser<A> extends AbstractParser<Option<A>>{
    private final Parser<? extends A> parserA;

    private OptionalParser(Parser<? extends A> parserA) {
        this.parserA = parserA;
    }

    public static <A> OptionalParser<A> optional(Parser<? extends A> parserA) {
        return new OptionalParser<A>(parserA);
    }

    @Override
    public String toString() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Result<Option<A>> call(Sequence<Character> characters) throws Exception {
        Result<? extends A> result = parserA.call(characters);
        if(result instanceof Success){
            Success<A> success = cast(result);
            return success(Option.some(result.value()), success.remainder());
        }
        return success(Option.<A>none(), characters);
    }
}
