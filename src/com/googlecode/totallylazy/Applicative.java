package com.googlecode.totallylazy;

public interface Applicative<A> extends Functor<A> { // Java does not support Type Constructors or classes statically implementing interfaces
    // Self<A> pure(A a);
    // <B> Self<B> applicate(Self<? extends Callable1<? super A, ? extends B>> applicator, Self<? extends A> option);
}
