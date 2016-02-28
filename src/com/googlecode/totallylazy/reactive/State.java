package com.googlecode.totallylazy.reactive;

import com.googlecode.totallylazy.Function;
import com.googlecode.totallylazy.Functor;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Value;

import java.util.NoSuchElementException;

import static com.googlecode.totallylazy.Unchecked.cast;

public interface State<A> extends Value<A>, Functor<A> {
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
