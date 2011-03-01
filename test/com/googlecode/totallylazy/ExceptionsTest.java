package com.googlecode.totallylazy;

import org.junit.Test;
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
}
