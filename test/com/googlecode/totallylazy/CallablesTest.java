package com.googlecode.totallylazy;

import org.junit.Test;

import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.Callables.curry;
import static com.googlecode.totallylazy.Callables.unCurry;
import static com.googlecode.totallylazy.matchers.NumberMatcher.is;
import static com.googlecode.totallylazy.numbers.Numbers.add;
import static org.hamcrest.MatcherAssert.assertThat;

public class CallablesTest {
    @Test
    public void canCurrySingleArgumentCallablesAsWell() throws Exception {
        final Function1<Number, Function1<Number, Number>> curriedAdd = curry(add());
        final Callable<Number> numberNumberCallable1 = curry(curriedAdd.call(1), 2);
        assertThat(numberNumberCallable1.call(), is(3));
    }

    @Test
    public void canCurryAdd() throws Exception {
        final Function1<Number, Function1<Number, Number>> curriedAdd = curry(add());
        assertThat(curriedAdd.call(1).call(2), is(3));
    }

    @Test
    public void canUnCurryAdd() throws Exception {
        final Function1<Number, Function1<Number, Number>> curriedAdd = curry(add());
        Callable2<Number, Number, Number> unCurriedAdd = unCurry(curriedAdd);
        assertThat(unCurriedAdd.call(1,2), is(3));
    }

}
