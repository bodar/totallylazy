package com.googlecode.totallylazy;

import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.Sequences.sequence;

public interface Quintuple<F, S, T, Fo, Fi> extends Quadruple<F, S, T, Fo>,  Fifth<Fi> {
    static abstract class AbstractQuintuple<F,S,T,Fo, Fi> extends AbstractQuadruple<F,S,T, Fo> implements Quintuple<F, S, T, Fo, Fi>{}

    public static <F, S, T, Fo, Fi> Quintuple<F, S, T, Fo, Fi> quintuple(final F first, final S second, final T third, final Fo fourth, final Fi fifth) {
        return new AbstractQuintuple<F, S, T, Fo, Fi>(){
            @Override public F first() { return first; }
            @Override public S second() { return second; }
            @Override public T third() { return third; }
            @Override public Fo fourth() { return fourth; }
            @Override public Fi fifth() { return fifth; }
        };
    }

    public static <F, S, T, Fo, Fi> Quintuple<F, S, T, Fo, Fi> quintuple(final Callable<? extends F> first, final Callable<? extends S> second, final Callable<? extends T> third, final Callable<? extends Fo> fourth, final Callable<? extends Fi> fifth) {
        return new AbstractQuintuple<F, S, T, Fo, Fi>(){
            @Override public F first() { return Functions.call(first); }
            @Override public S second() { return Functions.call(second); }
            @Override public T third() { return Functions.call(third); }
            @Override public Fo fourth() { return Functions.call(fourth); }
            @Override public Fi fifth() { return Functions.call(fifth); }
        };
    }

    @Override
    default Sequence<Object> values() {
        return sequence(first(), second(), third(), fourth(), fifth());
    }
}
