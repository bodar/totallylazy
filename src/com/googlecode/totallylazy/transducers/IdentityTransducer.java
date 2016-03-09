package com.googlecode.totallylazy.transducers;

public class IdentityTransducer<A> implements Transducer<A, A> {
    @Override
    public Receiver<A> apply(Receiver<A> receiver) {return receiver;}
}
