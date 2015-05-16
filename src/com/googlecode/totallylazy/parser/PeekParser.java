package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Segment;

import static com.googlecode.totallylazy.parser.Success.success;

class PeekParser<A> extends DelegateParser<A> {

    PeekParser(Parser<A> parser) {super(parser); }

    @Override
    public Result<A> parse(Segment<Character> characters) {
        Result<A> result = delegate.parse(characters);
        if(result.success()) return success(result.value(), characters);
        return result;
    }
}
