package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Sequence;

import static com.googlecode.totallylazy.Unchecked.cast;

public class MappingParser<A, B> extends BaseParser<B> {
    private final Parser<? extends A> source;
    private final Callable1<? super A, ? extends B> callable;

    private MappingParser(Parser<? extends A> source, Callable1<? super A, ? extends B> callable) {
        this.source = source;
        this.callable = callable;
    }

    public static <A, B> MappingParser<A, B> map(Parser<? extends A> source, Callable1<? super A, ? extends B> callable) {
        return new MappingParser<A, B>(source, callable);
    }

    @Override
    public Result<B> parse(Sequence<Character> characters) throws Exception {
        return cast(source.parse(characters).map(callable));
    }

    @Override
    public String toString() {
        return String.format("%s %s", source, callable);
    }
}
