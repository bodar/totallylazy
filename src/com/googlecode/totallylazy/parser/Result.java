package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Either;
import com.googlecode.totallylazy.Functor;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Segment;
import com.googlecode.totallylazy.Value;

public interface Result<A> extends Value<A>, Functor<A> {
    Segment<Character> remainder();

    @Override
    <B> Result<B> map(Callable1<? super A, ? extends B> callable);

    Option<A> option();

    Either<String, A> either();

    boolean success();

    boolean failure();

    String message();
}
