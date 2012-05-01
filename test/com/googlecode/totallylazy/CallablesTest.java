package com.googlecode.totallylazy;

import org.junit.Test;

import static com.googlecode.totallylazy.matchers.NumberMatcher.is;
import static com.googlecode.totallylazy.numbers.Numbers.add;
import static org.hamcrest.MatcherAssert.assertThat;

public class CallablesTest {
    @Test
    public void canDeferApplyOnSingleArgumentFunctions() throws Exception {
        Function1<Number, Number> add = add(1);
        assertThat(add.deferApply(2).call(), is(3));
        assertThat(Callables.deferApply(add, 2).call(), is(3));
    }

    @Test
    public void functionsAreInherantlyCurried() throws Exception {
        Function2<Number, Number, Number> add = add();
        Function1<Number, Function1<Number, Number>> curried = add;
        assertThat(curried.call(1).call(2), is(3));
        Function2<Number, Number, Number> callableAdd = add;
        assertThat(Callables.curry(callableAdd).call(1).call(2), is(3));
    }

    @Test
    public void canUnCurryAdd() throws Exception {
        Function1<Number, Function1<Number, Number>> curried = add();
        Callable2<Number, Number, Number> unCurriedAdd = Function2.uncurry2(curried);
        assertThat(unCurriedAdd.call(1,2), is(3));
    }

}
