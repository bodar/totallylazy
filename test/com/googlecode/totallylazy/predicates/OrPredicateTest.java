package com.googlecode.totallylazy.predicates;

import com.googlecode.totallylazy.matchers.Matchers;
import org.junit.Test;

import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Predicates.or;
import static org.hamcrest.MatcherAssert.assertThat;

public class OrPredicateTest {
    @Test
    public void supportsEqualityOnPredicateItself() throws Exception {
        assertThat(or(is("13"), is("14")).equals(or(is("13"), is("14"))), Matchers.is(true));
        assertThat(or(is("13"), is("14")).equals(or(is("13"), is("15"))), Matchers.is(false));
    }

    @Test
    public void supportsToString() throws Exception {
        assertThat(or(is("13"), is("14")).toString(), Matchers.is("13 or 14"));
    }

}
