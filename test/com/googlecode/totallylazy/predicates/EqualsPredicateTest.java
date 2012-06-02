package com.googlecode.totallylazy.predicates;

import org.junit.Test;

import static com.googlecode.totallylazy.matchers.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class EqualsPredicateTest {
    @Test
    public void supportsEqualOfThePredicateItself() throws Exception {
        assertThat(new EqualsPredicate<Integer>(2).equals(new EqualsPredicate<Integer>(2)), is(true));
        assertThat(new EqualsPredicate<Integer>(2).equals(new EqualsPredicate<Integer>(3)), is(false));
        assertThat(new EqualsPredicate<Integer>(2).equals(new EqualsPredicate<String>("2")), is(false));
    }

}
