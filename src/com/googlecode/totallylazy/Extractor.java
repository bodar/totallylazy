package com.googlecode.totallylazy;

import static com.googlecode.totallylazy.Sequences.sequence;

public interface Extractor<A, B> {
    Iterable<B> extract(A a);

    class functions {
        public static <A> Extractor<A, A> self() {
            return a -> sequence(a);
        }
    }
}
