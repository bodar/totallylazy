package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.*;

public interface Result<A> extends Value<A>, Functor<A> {
    Segment<Character> remainder();

    @Override
    <B> Result<B> map(Function1<? super A, ? extends B> callable);

    Option<A> option();

    Either<String, A> either();

    boolean success();

    boolean failure();

    String message();

}
