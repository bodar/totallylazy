package com.googlecode.totallylazy.reactive;

import com.googlecode.totallylazy.Function;
import com.googlecode.totallylazy.Functor;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Value;

import java.util.NoSuchElementException;

import static com.googlecode.totallylazy.Unchecked.cast;

public interface Observer<T> {
    void step(State<T> state);
}

interface State<A> extends Value<A>, Functor<A> {
    default boolean matches(Predicate<? super A> predicate) {
        return false;
    }

    @Override
    default <S> State<S> map(Function<? super A, ? extends S> callable) {
        return cast(this);
    }

    @Override
    default A value() {
        throw new NoSuchElementException();
    }
}

interface Next<A> extends State<A> {
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

interface Error<A> extends State<A> {
    Throwable throwable();

    static <A> Error<A> error(Throwable throwable) {
        return () -> throwable;
    }
}

interface Complete<A> extends State<A> {
    static <A> Complete<A> complete() {
        return new Complete<A>() { };
    }
}