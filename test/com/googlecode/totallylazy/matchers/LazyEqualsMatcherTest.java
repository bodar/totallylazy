package com.googlecode.totallylazy.matchers;

import org.hamcrest.Description;
import org.hamcrest.StringDescription;
import org.junit.Test;

import static com.googlecode.totallylazy.Callables.returns;
import static com.googlecode.totallylazy.matchers.LazyEqualsMatcher.lazyEqualTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNot.not;

public class LazyEqualsMatcherTest {
    private static final String DESCRIPTION_TEXT = "description";
    private static final String EXPECTED = "myObject";
    private static final String ACTUAL = EXPECTED;
    private static final String DIFFERENT_ACTUAL = "different";

    @Test
    public void shouldBeEqual() {
        assertThat(ACTUAL, lazyEqualTo(DESCRIPTION_TEXT, returns(EXPECTED)));
    }

    @Test
    public void shouldNotBeEqual() {
        assertThat(DIFFERENT_ACTUAL, not(lazyEqualTo(DESCRIPTION_TEXT, returns(EXPECTED))));
    }

    @Test
    public void shouldDescribeEquality() {
        Description description = new StringDescription();
        lazyEqualTo(DESCRIPTION_TEXT, returns(EXPECTED)).describeTo(description);
        assertThat(description.toString(), is(DESCRIPTION_TEXT));
    }
}
