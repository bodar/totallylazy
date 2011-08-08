package com.googlecode.totallylazy;

import org.junit.Test;

import static com.googlecode.totallylazy.Exceptions.captureException;
import static com.googlecode.totallylazy.Left.left;
import static com.googlecode.totallylazy.Right.right;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static com.googlecode.totallylazy.Callables.asCallable1;
import static com.googlecode.totallylazy.Callables.returnArgument;
import static com.googlecode.totallylazy.Callables.callThrows;
import static com.googlecode.totallylazy.Exceptions.handleException;
import static com.googlecode.totallylazy.Option.none;
import static com.googlecode.totallylazy.Option.option;
import static com.googlecode.totallylazy.Predicates.always;

public class ExceptionsTest {
    @Test
    public void supportsReturningNoneInsteadOfThrowingAnException() throws Exception {
        Callable1<Object, Object> throwingFunction = asCallable1(callThrows(new RuntimeException()));

        assertThat(handleException(throwingFunction,             always()).call(null), is((Option)none()));
        assertThat(handleException(returnArgument(String.class), always()).call("hi there"), is(option("hi there")));
    }

    @Test
    public void supportsCapturingExceptions() throws Exception {
        RuntimeException exception = new RuntimeException();
        Callable1<Object, Object> throwingFunction = asCallable1(callThrows(exception));

        assertThat(captureException(throwingFunction).call(null), is((Either)right(exception)));
        assertThat(captureException(returnArgument(String.class)).call("hi there"), is((Either)left("hi there")));
    }
}
