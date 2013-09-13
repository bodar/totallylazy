package com.googlecode.totallylazy;

import java.lang.reflect.Method;

import static com.googlecode.totallylazy.Predicates.any;

public abstract class pattern<A, B> extends Mapper<A, Option<B>> {
    @Override
    public Option<B> call(final A a) throws Exception {
        return new multi(any(Method.class)) {}.invoke(pattern.this, "match", a);
    }
}
