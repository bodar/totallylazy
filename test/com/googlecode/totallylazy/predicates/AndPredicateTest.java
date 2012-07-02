package com.googlecode.totallylazy.predicates;

import com.googlecode.totallylazy.matchers.Matchers;
import org.junit.Test;

import static com.googlecode.totallylazy.Predicates.and;
import static com.googlecode.totallylazy.Predicates.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class AndPredicateTest {

    @Test
    public void supportsEqualityOnPredicateItself() throws Exception {
        assertThat(and(is("13"), is("14")).equals(and(is("13"), is("14"))), Matchers.is(true));
        assertThat(and(is("13"), is("14")).equals(and(is("13"), is("15"))), Matchers.is(false));
    }

    @Test
    public void supportsToString() throws Exception {
        assertThat(and(is("13"), is("14")).toString(), Matchers.is("13 and 14"));
    }
}
