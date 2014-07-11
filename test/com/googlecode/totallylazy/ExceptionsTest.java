package com.googlecode.totallylazy;

import org.junit.Test;

import static com.googlecode.totallylazy.Assert.assertThat;
import static com.googlecode.totallylazy.Callables.returnArgument;
import static com.googlecode.totallylazy.Either.left;
import static com.googlecode.totallylazy.Either.right;
import static com.googlecode.totallylazy.Exceptions.either;
import static com.googlecode.totallylazy.Exceptions.handleException;
import static com.googlecode.totallylazy.Option.none;
import static com.googlecode.totallylazy.Option.option;
import static com.googlecode.totallylazy.Predicates.always;
import static com.googlecode.totallylazy.Predicates.is;

public class ExceptionsTest {
    @Test
    public void supportsReturningNoneInsteadOfThrowingAnException() throws Exception {
        Function<Object, Object> throwingFunction = o -> { throw new RuntimeException(); };

        assertThat(handleException(throwingFunction, always()).call(null), is((Option) none()));
        assertThat(handleException(returnArgument(String.class), always()).call("hi there"), is(option("hi there")));
    }

    @Test
    public void supportsCapturingExceptions() throws Exception {
        RuntimeException exception = new RuntimeException();
        Function<Object, Object> throwingFunction = o -> { throw exception; };

        assertThat(either(throwingFunction).call(null), is(left(exception)));
        assertThat(either(returnArgument(String.class)).call("hi there"), is(right("hi there")));
    }
}
