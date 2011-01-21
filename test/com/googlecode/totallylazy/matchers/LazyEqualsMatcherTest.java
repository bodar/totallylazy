package com.googlecode.totallylazy.matchers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.Test;

import static com.googlecode.totallylazy.Callables.returns;
import static com.googlecode.totallylazy.matchers.LazyEqualsMatcher.lazyEqualTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNot.not;

public class LazyEqualsMatcherTest {
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
        Description description = new StringDescription();
        lazyEqualTo(SIMPLE_DESCRIPTION_TEXT, returns(EXPECTED)).describeTo(description);
        assertThat(description.toString(), is(SIMPLE_DESCRIPTION_TEXT));
    }

    @Test
    public void shouldDescribeEqualityWithExpectedAndActual() {
        Description description = new StringDescription();
        Matcher<String> matcher = lazyEqualTo(SIMPLE_DESCRIPTION_TEXT, returns(EXPECTED));
        matcher.matches(DIFFERENT_ACTUAL);
        matcher.describeTo(description);
        assertThat(description.toString(), is(FULL_DESCRIPTION_TEXT));
    }
}