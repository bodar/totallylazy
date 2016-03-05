package com.googlecode.totallylazy.reactive;

public interface CompositeTransducer<A, B, C> extends Transducer<A, C> {
    Transducer<A, B> a();
    Transducer<B, C> b();

    static <A, B, C> CompositeTransducer<A, B, C> compositeTransducer(Transducer<A, B> a, Transducer<B, C> b) {
        return new CompositeTransducer<A,B,C>(){
            @Override public Transducer<A, B> a() { return a; }
            @Override public Transducer<B, C> b() { return b; }
        };
    }

    @Override
    default Observer<A> apply(Observer<C> observer) {return a().apply(b().apply(observer));}
}
