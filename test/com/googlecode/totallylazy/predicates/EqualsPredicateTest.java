package com.googlecode.totallylazy.predicates;

import org.junit.Test;

import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.predicates.EqualsPredicate.equalTo;
import static com.googlecode.totallylazy.Assert.assertThat;

public class EqualsPredicateTest {
    @Test
    public void supportsEqualOfThePredicateItself() throws Exception {
        assertThat(equalTo(2).equals(equalTo(2)), is(true));
        assertThat(equalTo(2).equals(equalTo(3)), is(false));
        assertThat(equalTo(2).equals(equalTo("2")), is(false));
    }

    @Test
    public void supportsNull() throws Exception {
        assertThat(equalTo(null).matches(null), is(true));
        assertThat(equalTo(null).matches(1), is(false));
        assertThat(equalTo(1).matches(null), is(false));
    }

    @Test
    public void supportsToString() throws Exception {
        assertThat(equalTo(1).toString(), is("1"));
        assertThat(equalTo(null).toString(), is("null"));
    }
}
