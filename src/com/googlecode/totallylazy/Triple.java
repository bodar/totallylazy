package com.googlecode.totallylazy;

import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.Sequences.sequence;

public interface Triple<F, S, T> extends Pair<F,S>, Third<T> {
    static abstract class AbstractTriple<F,S,T> extends AbstractPair<F,S> implements Triple<F,S,T>{}

    static <F, S, T> Triple<F, S, T> triple(final F first, final S second, final T third) {
        return new AbstractTriple<F, S, T>(){
            @Override public F first() { return first; }
            @Override public S second() { return second; }
            @Override public T third() { return third; }
        };
    }

    static <F, S, T> Triple<F, S, T> triple(final Callable<? extends F> first, final Callable<? extends S> second, final Callable<? extends T> third) {
        return new AbstractTriple<F, S, T>(){
            @Override public F first() { return Functions.call(first); }
            @Override public S second() { return Functions.call(second); }
            @Override public T third() { return Functions.call(third); }
        };
    }

    @Override default Seq<Object> values() { return sequence(first(), second(), third()); }

    static <A, B, C, D> Triple<B, C, D> leftShift(Triple<A, B, C> triple, D d) {
        return Triple.triple(triple.second(), triple.third(), d);
    }
}
