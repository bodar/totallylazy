package com.googlecode.totallylazy.matchers;

import org.junit.Test;

import static com.googlecode.totallylazy.Assert.assertThat;
import static com.googlecode.totallylazy.Predicates.not;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.matchers.IterablePredicates.hasSize;

public class HasSizeMatcherTest {
    @Test
    public void shouldMatchIterablesWithExpectedSize() {
        assertThat(sequence(1, 2, 3), hasSize(3));
        assertThat(sequence("a", 1, 2), not(hasSize(4)));
    }
}
