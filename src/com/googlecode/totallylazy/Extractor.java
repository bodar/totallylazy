package com.googlecode.totallylazy;

import static com.googlecode.totallylazy.Sequences.sequence;

public interface Extractor<A, B> {
    Iterable<B> extract(A a);

    class functions {
        public static <A> Extractor<A, A> self() {
            return new Extractor<A, A>() {
                @Override
                public Iterable<A> extract(A a) {
                    return sequence(a);
                }
            };
        }
    }
}
