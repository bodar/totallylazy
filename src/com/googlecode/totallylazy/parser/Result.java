package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Either;
import com.googlecode.totallylazy.Functor;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Segment;
import com.googlecode.totallylazy.Value;
import com.googlecode.totallylazy.functions.Function1;

public interface Result<A> extends Value<A>, Functor<A> {
    Object actual();
    Segment<Character> remainder();

    @Override
    <B> Result<B> map(Function1<? super A, ? extends B> callable);

    Option<A> option();

    Either<String, A> either();

    boolean success();

    boolean failure();

    String message();

    class functions {
        public static <A> Function1<Result<A>, Segment<Character>> remainder() {
            return Result::remainder;
        }
        public static <A> Function1<Result<A>, Object> actual() {
            return Result::actual;
        }
    }
}
