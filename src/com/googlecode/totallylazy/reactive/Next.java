package com.googlecode.totallylazy.reactive;

import com.googlecode.totallylazy.Function;
import com.googlecode.totallylazy.Predicate;

public interface Next<A> extends State<A> {
    static <A> Next<A> next(A a) {
        return new Next<A>() {
            @Override
            public A value() {return a;}

            @Override
            public boolean matches(Predicate<? super A> predicate) {
                return predicate.matches(value());
            }

            @Override
            public <S> State<S> map(Function<? super A, ? extends S> callable) {
                return Next.next(callable.apply(value()));
            }
        };
    }
}
