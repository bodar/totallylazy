package com.googlecode.totallylazy.matchers;

import com.googlecode.totallylazy.Strings;
import org.junit.Test;

import static com.googlecode.totallylazy.Callables.returnArgument;
import static com.googlecode.totallylazy.Callables.returns;
import static com.googlecode.totallylazy.Predicates.not;
import static com.googlecode.totallylazy.matchers.LazyMatcher.matchesLazily;
import static com.googlecode.totallylazy.PredicateAssert.assertThat;
import static com.googlecode.totallylazy.Predicates.is;

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
        assertThat(matchesLazily(DESCRIPTION_TEXT, EXPECTED, returnArgument(String.class)).toString(), is(DESCRIPTION_TEXT));
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
        assertThat(matchesLazily(DESCRIPTION_TEXT, returns(EXPECTED), returnArgument(String.class)).toString(), is(DESCRIPTION_TEXT));
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
        assertThat(matchesLazily(is(EXPECTED), returnArgument(String.class)).toString(), Strings.contains(EXPECTED));
    }
}