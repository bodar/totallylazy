package com.googlecode.totallylazy;

import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.Sequences.sequence;

public interface Quadruple<F, S, T, Fo> extends Triple<F, S, T> , Fourth<Fo> {
    static abstract class AbstractQuadruple<F,S,T,Fo> extends AbstractTriple<F,S,T> implements Quadruple<F,S,T,Fo>{}

    static <F, S, T, Fo> Quadruple<F, S, T, Fo> quadruple(final F first, final S second, final T third, final Fo fourth) {
        return new AbstractQuadruple<F, S, T, Fo>(){
            @Override public F first() { return first; }
            @Override public S second() { return second; }
            @Override public T third() { return third; }
            @Override public Fo fourth() { return fourth; }
        };
    }

    static <F, S, T, Fo> Quadruple<F, S, T, Fo> quadruple(final Callable<? extends F> first, final Callable<? extends S> second, final Callable<? extends T> third, final Callable<? extends Fo> fourth) {
        return new AbstractQuadruple<F, S, T, Fo>(){
            @Override public F first() { return Functions.call(first); }
            @Override public S second() { return Functions.call(second); }
            @Override public T third() { return Functions.call(third); }
            @Override public Fo fourth() { return Functions.call(fourth); }
        };
    }

    @Override
    default Seq<Object> values() {
        return sequence(first(), second(), third(), fourth());
    }
}
