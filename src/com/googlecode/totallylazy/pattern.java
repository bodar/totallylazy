package com.googlecode.totallylazy;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static com.googlecode.totallylazy.Predicates.any;
import static com.googlecode.totallylazy.Sequences.sequence;

public abstract class pattern {
    private final List<extract> extracts = new ArrayList<extract>();
    private final Object[] argument;

    public pattern(Object... argument) {
        this.argument = argument;
    }

    public pattern(Iterable<?> arguments) {
        this.argument = sequence(arguments).toArray(Object.class);
    }

    public <T> T match() {
        return this.<T>matchOption().get();
    }

    public <T> Option<T> matchOption() {
        Option<T> foo = sequence(extracts).flatMap(functions.<T>matchOption()).headOption();
        return foo.orElse(new multi(any(Method.class)) {}.<T>methodFor(Methods.method(getClass(), "match").get(), argument));
    }

    protected class extract extends pattern {
        public extract(Extractor<?,?> extractor) {
            super(Unchecked.<Extractor<Object,Object>>cast(extractor).extract(pattern.this.argument[0]));
            pattern.this.extracts.add(this);
        }
    }

    public static class functions{
        public static <T> Mapper<pattern, Option<T>> matchOption() {
            return new Mapper<pattern, Option<T>>() {
                @Override
                public Option<T> call(pattern pattern) throws Exception {
                    return pattern.matchOption();
                }
            };
        }
    }
}
