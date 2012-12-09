package com.googlecode.totallylazy.matchers;

import org.hamcrest.Matcher;
import org.junit.Test;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.matchers.IterableMatcher.hasExactlyMatching;
import static com.googlecode.totallylazy.matchers.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class IterableMatcherTest {
    @Test
    public void matchesAgainstValues() throws Exception {
        Matcher<Iterable<Integer>> matcher = IterableMatcher.hasExactly(1, 2);
        assertTrue(matcher.matches(sequence(1, 2)));
        assertFalse(matcher.matches(sequence(1, 2, 3)));
    }

    @Test
    public void matchesAgainstIterablesOfMatchers() throws Exception {
        Matcher<Iterable<Integer>> matcher = hasExactlyMatching(sequence(is(1), is(2)));
        assertTrue(matcher.matches(sequence(1, 2)));
        assertFalse(matcher.matches(sequence(1, 3)));
        assertFalse(matcher.matches(sequence(1)));
        assertFalse(matcher.matches(sequence(1, 2, 3)));
    }
}
