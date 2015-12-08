package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Segment;

class PrettyParser<A> extends Parser<A> {
    private final Parse<A> parse;
    private final String pretty;

    private PrettyParser(Parse<A> parse, String pretty) {
        this.parse = parse;
        this.pretty = pretty;
    }

    static <A> PrettyParser<A> pretty(Parse<A> parse, String pretty) {return new PrettyParser<A>(parse, pretty);}

    @Override
    public String toString() {
        return pretty;
    }

    @Override
    public Result<A> parse(Segment<Character> characters) {
        Result<A> result = parse.parse(characters);
        if (result.success())
            return result;
        return fail(toString(), result.actual());
    }
}
