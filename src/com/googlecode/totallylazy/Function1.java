package com.googlecode.totallylazy;

public abstract class Function1<A, B> implements Callable1<A,B>{
    public B apply(final A a){
        return Callers.call(this, a);
    }

    public Function<B> curry(final A a){
        return Callables.curry(this, a);
    }

}
