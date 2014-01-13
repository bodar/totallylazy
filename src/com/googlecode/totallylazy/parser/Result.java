package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Function;
import com.googlecode.totallylazy.Functor;
import com.googlecode.totallylazy.Value;

import java.nio.CharBuffer;

public interface Result<A> extends Value<A>, Functor<A> {
    CharBuffer remainder();

    @Override
    <B> Result<B> map(Function<? super A, ? extends B> callable);
}
