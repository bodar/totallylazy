package com.googlecode.totallylazy;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import static com.googlecode.totallylazy.Callables.when;
import static com.googlecode.totallylazy.Functions.constant;
import static com.googlecode.totallylazy.matchers.NumberMatcher.hasExactly;
import static com.googlecode.totallylazy.matchers.NumberMatcher.is;
import static com.googlecode.totallylazy.numbers.Numbers.add;
import static com.googlecode.totallylazy.numbers.Numbers.even;
import static com.googlecode.totallylazy.numbers.Numbers.primes;
import static org.hamcrest.CoreMatchers.nullValue;
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
        Callable2<Number, Number, Number> unCurriedAdd = Functions.uncurry2(curried);
        assertThat(unCurriedAdd.call(1, 2), is(3));
    }

    @Test
    public void canReplaceInline() {
        assertThat(primes().map(when(even(), constant((Number) 0))).take(5), hasExactly(0, 3, 5, 7, 11));
    }

    @Test
    public void canInvokeToStringOnNull() {
        assertThat(Callables.toString.apply(null), CoreMatchers.is(nullValue()));
    }
}
