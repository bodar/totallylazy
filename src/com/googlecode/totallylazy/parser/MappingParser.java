package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Function;

import java.nio.CharBuffer;

public class MappingParser<A, B> extends Parser<B> {
    private final Parse<? extends A> source;
    private final Function<? super A, ? extends B> callable;

    private MappingParser(Parse<? extends A> source, Function<? super A, ? extends B> callable) {
        this.source = source;
        this.callable = callable;
    }

    public static <A, B> MappingParser<A, B> map(Parse<? extends A> source, Function<? super A, ? extends B> callable) {
        return new MappingParser<A, B>(source, callable);
    }

    @Override
    public Result<B> parse(CharBuffer characters) throws Exception {
        return source.parse(characters).map(callable);
    }

    @Override
    public String toString() {
        return String.format("%s %s", source, callable);
    }
}
