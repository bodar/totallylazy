package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Function1;
import com.googlecode.totallylazy.Functor;
import com.googlecode.totallylazy.Segment;

public interface Parse<A> extends Functor<A> {
    Result<A> parse(Segment<Character> characters);

    @Override
    <S> Parse<S> map(Callable1<? super A, ? extends S> callable);

    class functions {
        public static <A> Function1<Segment<Character>, Result<A>> parse(final Parse<A> parser) {
            return new Function1<Segment<Character>, Result<A>>() {
                @Override
                public Result<A> call(Segment<Character> characterSegment) throws Exception {
                    return parser.parse(characterSegment);
                }
            };
        }
    }

    class methods {

    }
}
