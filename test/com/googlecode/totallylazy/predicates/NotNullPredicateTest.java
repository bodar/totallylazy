package com.googlecode.totallylazy.predicates;

import org.junit.Test;

import static com.googlecode.totallylazy.matchers.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class NotNullPredicateTest {

    @Test
    public void supportsEqualOfThePredicateItself() throws Exception {
        assertThat(new NotNullPredicate<String>().equals(new NotNullPredicate<String>()), is(true));
        assertThat(new NotNullPredicate<String>().equals("some object that's not the predicate"), is(false));
    }

    @Test
    public void supportsToString() throws Exception {
        assertThat(new NotNullPredicate<Integer>().toString(), is("is not null"));
    }
}
