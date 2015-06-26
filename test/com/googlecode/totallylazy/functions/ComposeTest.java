package com.googlecode.totallylazy.functions;

import org.hamcrest.MatcherAssert;
import org.junit.Test;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.functions.Compose.compose;
import static com.googlecode.totallylazy.matchers.NumberMatcher.is;
import static com.googlecode.totallylazy.numbers.Numbers.add;
import static com.googlecode.totallylazy.numbers.Numbers.multiply;
import static org.hamcrest.MatcherAssert.assertThat;

public class ComposeTest {
    @Test
    public void canComposeFunctions() throws Exception {
        MatcherAssert.assertThat(sequence(add(2), multiply(3)).reduce(Compose.compose(Number.class)).apply(1), is(9));
    }
}
