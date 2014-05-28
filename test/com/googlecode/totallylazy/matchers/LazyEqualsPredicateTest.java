package com.googlecode.totallylazy.matchers;

import org.junit.Test;

import static com.googlecode.totallylazy.Callables.returns;
import static com.googlecode.totallylazy.Assert.assertThat;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Predicates.not;
import static com.googlecode.totallylazy.matchers.LazyEqualsPredicate.lazyEqualTo;

public class LazyEqualsPredicateTest {
    private static final String SIMPLE_DESCRIPTION_TEXT = "description";
    private static final String EXPECTED = "myObject";
    private static final String ACTUAL = EXPECTED;
    private static final String DIFFERENT_ACTUAL = "different";
    private static final String FULL_DESCRIPTION_TEXT = SIMPLE_DESCRIPTION_TEXT + " [expected: " + EXPECTED + ", actual: " + DIFFERENT_ACTUAL + "]";

    @Test
    public void shouldBeEqual() {
        assertThat(ACTUAL, lazyEqualTo(SIMPLE_DESCRIPTION_TEXT, returns(EXPECTED)));
    }

    @Test
    public void shouldNotBeEqual() {
        assertThat(DIFFERENT_ACTUAL, not(lazyEqualTo(SIMPLE_DESCRIPTION_TEXT, returns(EXPECTED))));
    }

    @Test
    public void shouldDescribeEquality() {
        assertThat(lazyEqualTo(SIMPLE_DESCRIPTION_TEXT, returns(EXPECTED)).toString(), is(SIMPLE_DESCRIPTION_TEXT));
    }

    @Test
    public void shouldDescribeEqualityWithExpectedAndActual() {
        LazyEqualsPredicate<String> predicate = lazyEqualTo(SIMPLE_DESCRIPTION_TEXT, returns(EXPECTED));
        predicate.matches(DIFFERENT_ACTUAL); // this is evil
        assertThat(predicate.toString(), is(FULL_DESCRIPTION_TEXT));
    }
}