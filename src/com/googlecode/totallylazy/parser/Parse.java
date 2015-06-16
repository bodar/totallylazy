package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Function1;
import com.googlecode.totallylazy.Functor;
import com.googlecode.totallylazy.Segment;

public interface Parse<A> extends Functor<A> {
    Result<A> parse(Segment<Character> characters);

    @Override
    <S> Parse<S> map(Function1<? super A, ? extends S> callable);

}
