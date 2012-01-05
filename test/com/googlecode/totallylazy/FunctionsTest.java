package com.googlecode.totallylazy;

import com.googlecode.totallylazy.matchers.NumberMatcher;
import org.junit.Test;

import static com.googlecode.totallylazy.Callables.compose;
import static com.googlecode.totallylazy.numbers.Numbers.add;
import static com.googlecode.totallylazy.numbers.Numbers.multiply;
import static org.hamcrest.MatcherAssert.assertThat;

public class FunctionsTest {
    @Test
    public void canComposeFunctions() throws Exception {
        assertThat(compose(add(10), multiply(3)).apply(2), NumberMatcher.is(36));
        assertThat(add(10).map(multiply(3)).apply(2), NumberMatcher.is(36));
        assertThat(add(10).then(multiply(3)).apply(2), NumberMatcher.is(36));
        assertThat(multiply(3).following(add(10)).apply(2), NumberMatcher.is(36));
    }
}
