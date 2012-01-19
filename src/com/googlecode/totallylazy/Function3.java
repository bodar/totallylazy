package com.googlecode.totallylazy;

public abstract class Function3<A,B,C,D> extends Function1<A, Function2<B, C, D>> implements Callable3<A,B,C,D>{
    public Function1<A, Function1<B, Function1<C, D>>> curry() {
        return Callables.curry(this);
    }

    @Override
    public Function2<B, C, D> call(final A a) throws Exception {
        return Callables.partial((Callable3<A, B, C, D>) this, a);
    }


}
