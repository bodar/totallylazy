package com.googlecode.totallylazy;

import com.googlecode.totallylazy.matchers.IterablePredicates;
import org.junit.Test;

import static com.googlecode.totallylazy.Callables.when;
import static com.googlecode.totallylazy.Functions.constant;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.matchers.NumberMatcher.is;
import static com.googlecode.totallylazy.numbers.Numbers.add;
import static com.googlecode.totallylazy.numbers.Numbers.even;
import static com.googlecode.totallylazy.numbers.Numbers.primes;
import static com.googlecode.totallylazy.PredicateAssert.assertThat;

public class CallablesTest {
    @Test
    public void canDeferApplyOnSingleArgumentFunctions() throws Exception {
        Function<Number, Number> add = add(1);
        assertThat(add.deferApply(2).call(), is(3));
        assertThat(Callables.deferApply(add, 2).call(), is(3));
    }

    @Test
    public void functionsAreInherantlyCurried() throws Exception {
        Function2<Number, Number, Number> add = add();
        Function<Number, Function<Number, Number>> curried = add;
        assertThat(curried.call(1).call(2), is(3));
        Function2<Number, Number, Number> callableAdd = add;
        assertThat(Callables.curry(callableAdd).call(1).call(2), is(3));
    }

    @Test
    public void canUnCurryAdd() throws Exception {
        Function<Number, Function<Number, Number>> curried = add();
        Function2<Number, Number, Number> unCurriedAdd = Functions.uncurry2(curried);
        assertThat(unCurriedAdd.call(1, 2), is(3));
    }

    @Test
    public void canReplaceInline() {
        assertThat(primes().map(when(even(), constant((Number) 0))).take(5), IterablePredicates.<Number>hasExactly(sequence(0, 3, 5, 7, 11)));
    }
}
