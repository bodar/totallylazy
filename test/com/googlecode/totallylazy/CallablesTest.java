package com.googlecode.totallylazy;

import org.junit.Test;

import static com.googlecode.totallylazy.Callables.add;
import static com.googlecode.totallylazy.Callables.curry;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class CallablesTest {
    @Test
    public void canCurryAdd() throws Exception {
        assertThat(curry(add()).call(1).call(2), is(3));
    }
}
