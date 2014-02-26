package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Segment;
import com.googlecode.totallylazy.Sequence;

import static com.googlecode.totallylazy.Sequences.sequence;

public class OrParser<A> extends Parser<A> {
    private final Sequence<Parse<A>> parsers;

    public OrParser(Sequence<Parse<A>> parsers) {
        this.parsers = parsers;
    }

    public static <A> OrParser<A> or(Iterable<? extends Parse<? extends A>> parsers) {
        return new OrParser<A>(sequence(parsers).<Parse<A>>unsafeCast());
    }

    @Override
    public String toString() {
        return parsers.toString(" or ");
    }

    @Override
    public Result<A> parse(Segment<Character> characters) {
        Result<A> result = null;
        for (Parse<A> parser : parsers) {
            result = parser.parse(characters);
            if(result.success()) return result;
        }
        return result;
    }
}
