package com.googlecode.totallylazy.matchers;

import org.junit.Test;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.matchers.IterableMatcher.hasSize;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;

public class HasSizeMatcherTest {
    @Test
    public void shouldMatchIterablesWithExpectedSize() {
        assertThat(sequence(1, 2, 3), hasSize(3));
        assertThat(sequence("a", 1, 2), not(hasSize(4)));
    }
}
