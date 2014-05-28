package com.googlecode.totallylazy.matchers;

import com.googlecode.totallylazy.Predicate;
import org.junit.Test;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.matchers.IterablePredicates.hasExactlyMatching;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Assert.assertFalse;
import static com.googlecode.totallylazy.Assert.assertTrue;

public class IterablePredicatesTest {
    @Test
    public void matchesAgainstValues() throws Exception {
        Predicate<Iterable<Integer>> matcher = IterablePredicates.hasExactly(1, 2);
        assertTrue(matcher.matches(sequence(1, 2)));
        assertFalse(matcher.matches(sequence(1, 2, 3)));
    }

    @Test
    public void matchesAgainstIterablesOfMatchers() throws Exception {
        Predicate<Iterable<Integer>> matcher = hasExactlyMatching(sequence(is(1), is(2)));
        assertTrue(matcher.matches(sequence(1, 2)));
        assertFalse(matcher.matches(sequence(1, 3)));
        assertFalse(matcher.matches(sequence(1)));
        assertFalse(matcher.matches(sequence(1, 2, 3)));
    }
}
