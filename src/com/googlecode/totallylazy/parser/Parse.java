package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Function;
import com.googlecode.totallylazy.Functor;
import com.googlecode.totallylazy.Segment;

public interface Parse<A> extends Functor<A> {
    Result<A> parse(Segment<Character> characters);

    @Override
    <S> Parse<S> map(Function<? super A, ? extends S> callable);

    class functions {
        public static <A> Function<Segment<Character>, Result<A>> parse(final Parse<A> parser) {
            return new Function<Segment<Character>, Result<A>>() {
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
