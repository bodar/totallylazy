package com.googlecode.totallylazy.comparators;

import com.googlecode.totallylazy.numbers.Integers;
import org.junit.Test;

import static com.googlecode.totallylazy.PredicateAssert.assertThat;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.matchers.NumberMatcher.is;

public class MaximumTest {
    @Test
    public void nullsMoveToEnd() {
        assertThat(sequence(1, 2, null).reduce(Integers.maximum()), is(2));
        assertThat(sequence(null, 2, null).reduce(Integers.maximum()), is(2));
    }
}
