package com.googlecode.totallylazy.comparators;

import com.googlecode.totallylazy.numbers.Integers;
import org.junit.Test;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.matchers.NumberMatcher.is;
import static com.googlecode.totallylazy.PredicateAssert.assertThat;

public class MinimumTest {
    @Test
    public void nullsMoveToEnd() {
        assertThat(sequence(1, 2, null).reduce(Integers.minimum()), is(1));
        assertThat(sequence(null, 2, null).reduce(Integers.minimum()), is(2));
    }
}
