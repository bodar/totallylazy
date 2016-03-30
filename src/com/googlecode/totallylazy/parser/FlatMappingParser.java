package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Segment;
import com.googlecode.totallylazy.functions.Function1;

import static com.googlecode.totallylazy.Unchecked.cast;

class FlatMappingParser<A, B> implements Parser<B> {
    private final Parser<? extends A> source;
    private final Function1<? super A, ? extends Result<B>> callable;

    private FlatMappingParser(Parser<? extends A> source, Function1<? super A, ? extends Result<B>> callable) {
        this.source = source;
        this.callable = callable;
    }

    public static <A, B> FlatMappingParser<A, B> flatMap(Parser<? extends A> source, Function1<? super A, ? extends Result<B>> callable) {
        return new FlatMappingParser<A, B>(source, callable);
    }

    @Override
    public Result<B> parse(Segment<Character> characters) {
        return source.parse(characters).flatMap(callable);
    }

    @Override
    public String toString() {
        return String.format("%s %s", source, callable);
    }
}
