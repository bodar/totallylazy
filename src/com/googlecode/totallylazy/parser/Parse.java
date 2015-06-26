package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.functions.Function1;
import com.googlecode.totallylazy.Functor;
import com.googlecode.totallylazy.Segment;

public interface Parse<A> extends Functor<A> {
    Result<A> parse(Segment<Character> characters);

    @Override
    <S> Parse<S> map(Function1<? super A, ? extends S> callable);

    class functions {
        public static <A> Function1<Segment<Character>, Result<A>> parse(final Parse<A> parser) {
            return parser::parse;
        }
    }

    class methods {

    }
}
