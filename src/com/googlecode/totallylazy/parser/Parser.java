package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Functor;
import com.googlecode.totallylazy.Segment;

public interface Parser<A> extends Functor<A> {
    Result<A> parse(Segment<Character> sequence) throws Exception;

    @Override
    <S> Parser<S> map(Callable1<? super A, ? extends S> callable);
}
