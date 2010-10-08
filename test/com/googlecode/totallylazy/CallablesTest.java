package com.googlecode.totallylazy;

import org.junit.Test;

import static com.googlecode.totallylazy.Callables.add;
import static com.googlecode.totallylazy.Callables.curry;
import static com.googlecode.totallylazy.Callables.unCurry;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class CallablesTest {
    @Test
    public void canCurryAdd() throws Exception {
        final Callable1<Integer, Callable1<Integer, Integer>> curriedAdd = curry(add());
        assertThat(curriedAdd.call(1).call(2), is(3));
    }

    @Test
    public void canUnCurryAdd() throws Exception {
        final Callable1<Integer, Callable1<Integer, Integer>> curriedAdd = curry(add());
        Callable2<Integer, Integer, Integer> unCurriedAdd = unCurry(curriedAdd);
        assertThat(unCurriedAdd.call(1,2), is(3));
    }
}
