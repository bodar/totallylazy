package com.googlecode.totallylazy;

import java.lang.reflect.Method;

import static com.googlecode.totallylazy.Predicates.any;
import static com.googlecode.totallylazy.Sequences.sequence;

public abstract class match<A, B> extends Mapper<A, Option<B>> {
    private final Extractor<? super A, ?> extractor;

    public match(Extractor<? super A, ?> extractor) { this.extractor = extractor; }
    public match() { this(Extractor.functions.<A>self()); }

    @Override
    public Option<B> call(final A a) throws Exception {
        return new multi(any(Method.class)) {}.
                invoke(match.this, "value", sequence(extractor.extract(a)).toArray(Object.class));
    }
}
