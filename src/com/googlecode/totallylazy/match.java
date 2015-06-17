package com.googlecode.totallylazy;

import static com.googlecode.totallylazy.Sequences.sequence;

public abstract class match<A, B> implements Function1<A, Option<B>> {
    private final Extractor<? super A, ?> extractor;
    private final Dispatcher dispatcher;

    public match(Extractor<? super A, ?> extractor) {
        this.extractor = extractor;
        dispatcher = Dispatcher.dispatcher(this, "value");

    }
    public match() { this(Extractor.functions.<A>self()); }

    @Override
    public Option<B> call(final A a) throws Exception {
        return dispatcher.invokeOption(sequence(extractor.extract(a)).toArray(Object.class));
    }
}
