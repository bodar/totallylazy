package com.googlecode.totallylazy.numbers;

import org.junit.Test;

import static com.googlecode.totallylazy.matchers.IterableMatcher.hasExactly;
import static com.googlecode.totallylazy.matchers.IterableMatcher.startsWith;
import static com.googlecode.totallylazy.numbers.Integers.range;
import static org.hamcrest.MatcherAssert.assertThat;

public class IntegersTest {
    @Test
    public void supportsRange() throws Exception {
        assertThat(range(0), startsWith(0, 1, 2, 3, 4, 5, 6));
        assertThat(range(1, 5), hasExactly(1, 2, 3, 4, 5));
        assertThat(range(5, 1), hasExactly(5, 4, 3, 2, 1));
        assertThat(range(0, 4, 2), hasExactly(0, 2, 4));
        assertThat(range(4, 0, -2), hasExactly(4, 2, 0));
    }
}
