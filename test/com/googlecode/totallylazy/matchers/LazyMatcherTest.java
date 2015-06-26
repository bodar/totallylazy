package com.googlecode.totallylazy.matchers;

import org.hamcrest.Description;
import org.hamcrest.StringDescription;
import org.junit.Test;

import static com.googlecode.totallylazy.functions.Callables.returnArgument;
import static com.googlecode.totallylazy.functions.Callables.returns;
import static com.googlecode.totallylazy.matchers.LazyMatcher.matchesLazily;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNot.not;

public class LazyMatcherTest {
    private static final String DESCRIPTION_TEXT = "description";
    private static final String EXPECTED = "myObject";
    private static final String ACTUAL = EXPECTED;
    private static final String DIFFERENT_ACTUAL = "different";

    @Test
    public void shouldMatchExpected() {
        assertThat(ACTUAL, matchesLazily(DESCRIPTION_TEXT, EXPECTED, returnArgument(String.class)));
    }
    
    @Test
    public void shouldNotMatchExpected() {
        assertThat(DIFFERENT_ACTUAL, not(matchesLazily(DESCRIPTION_TEXT, EXPECTED, returnArgument(String.class))));
    }

    @Test
    public void shouldDescribeExpected() {
        Description description = new StringDescription();
        matchesLazily(DESCRIPTION_TEXT, EXPECTED, returnArgument(String.class)).describeTo(description);
        assertThat(description.toString(), is(DESCRIPTION_TEXT));
    }

    @Test
    public void shouldMatchExpectedLoader() {
        assertThat(ACTUAL, matchesLazily(DESCRIPTION_TEXT, returns(EXPECTED), returnArgument(String.class)));
    }

    @Test
    public void shouldNotMatchExpectedLoader() {
        assertThat(DIFFERENT_ACTUAL, not(matchesLazily(DESCRIPTION_TEXT, returns(EXPECTED), returnArgument(String.class))));
    }

    @Test
    public void shouldDescribeExpectedLoader() {
        Description description = new StringDescription();
        matchesLazily(DESCRIPTION_TEXT, returns(EXPECTED), returnArgument(String.class)).describeTo(description);
        assertThat(description.toString(), is(DESCRIPTION_TEXT));
    }

    @Test
    public void shouldMatchExpectedMatcher() {
        assertThat(ACTUAL, matchesLazily(is(EXPECTED), returnArgument(String.class)));
    }

    @Test
    public void shouldNotMatchExpectedMatcher() {
        assertThat(DIFFERENT_ACTUAL, not(matchesLazily(is(EXPECTED), returnArgument(String.class))));
    }

    @Test
    public void shouldDescribeExpectedMatcher() {
        Description description = new StringDescription();
        matchesLazily(is(EXPECTED), returnArgument(String.class)).describeTo(description);
        assertThat(description.toString(), containsString(EXPECTED));
    }
}