package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Function1;
import com.googlecode.totallylazy.Functor;
import com.googlecode.totallylazy.Segment;

import java.nio.CharBuffer;

public interface Parse<A> extends Functor<A> {
    Result<A> parse(CharBuffer sequence) throws Exception;

    @Override
    <S> Parse<S> map(Callable1<? super A, ? extends S> callable);

    class functions {
        public static <A> Function1<CharBuffer, Result<A>> parse(final Parse<A> parser) {
            return new Function1<CharBuffer, Result<A>>() {
                @Override
                public Result<A> call(CharBuffer characterSegment) throws Exception {
                    return parser.parse(characterSegment);
                }
            };
        }
    }
}
