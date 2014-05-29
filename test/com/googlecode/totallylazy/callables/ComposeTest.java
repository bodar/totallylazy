package com.googlecode.totallylazy.callables;

import org.junit.Test;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.callables.Compose.compose;
import static com.googlecode.totallylazy.matchers.NumberMatcher.is;
import static com.googlecode.totallylazy.numbers.Numbers.add;
import static com.googlecode.totallylazy.numbers.Numbers.multiply;
import static org.hamcrest.MatcherAssert.assertThat;

public class ComposeTest {
    @Test
    public void canComposeFunctions() throws Exception {
        assertThat(sequence(add(2), multiply(3)).reduce(compose(Number.class)).apply(1), is(9));
    }
}
