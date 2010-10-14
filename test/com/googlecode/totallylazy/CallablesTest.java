package com.googlecode.totallylazy;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.Callables.curry;
import static com.googlecode.totallylazy.Callables.entryToPair;
import static com.googlecode.totallylazy.Callables.unCurry;
import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.numbers.Numbers.add;
import static com.googlecode.totallylazy.predicates.IterableMatcher.hasExactly;
import static com.googlecode.totallylazy.predicates.NumberMatcher.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class CallablesTest {
    @Test
    public void supportsConvertingMapEntriesToPairs() throws Exception {
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("Dan", 2);

        assertThat(sequence(map.entrySet()).map(Callables.<String, Integer>entryToPair()), hasExactly(pair("Dan", 2)));
    }

    @Test
    public void canCurrySingleArgumentCallablesAsWell() throws Exception {
        final Callable1<Number, Callable1<Number, Number>> curriedAdd = curry(add());
        final Callable<Number> numberNumberCallable1 = curry(curriedAdd.call(1), 2);
        assertThat(numberNumberCallable1.call(), is(3));
    }

    @Test
    public void canCurryAdd() throws Exception {
        final Callable1<Number, Callable1<Number, Number>> curriedAdd = curry(add());
        assertThat(curriedAdd.call(1).call(2), is(3));
    }

    @Test
    public void canUnCurryAdd() throws Exception {
        final Callable1<Number, Callable1<Number, Number>> curriedAdd = curry(add());
        Callable2<Number, Number, Number> unCurriedAdd = unCurry(curriedAdd);
        assertThat(unCurriedAdd.call(1,2), is(3));
    }

}
