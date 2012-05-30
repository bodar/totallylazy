package com.googlecode.totallylazy.predicates;

import org.junit.Test;

import static com.googlecode.totallylazy.matchers.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class NotEqualsPredicateTest {

    @Test
    public void supportsEqualOfThePredicateItself() throws Exception {
        assertThat(new NotEqualsPredicate<Integer>(2).equals(new NotEqualsPredicate<Integer>(2)), is(true));
        assertThat(new NotEqualsPredicate<Integer>(2).equals(new NotEqualsPredicate<Integer>(3)), is(false));
    }

    @Test
    public void supportsToString() throws Exception {
        assertThat(new NotEqualsPredicate<Integer>(2).toString(), is("is not 2"));
    }

}
