package com.googlecode.totallylazy.predicates;

import org.junit.Test;

import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Predicates.not;
import static com.googlecode.totallylazy.predicates.LessThanPredicate.lessThan;
import static com.googlecode.totallylazy.PredicateAssert.assertThat;

public class LessThanPredicateTest {
    @Test
    public void supportsToString() throws Exception {
        assertThat(lessThan(2).toString(), is("lessThan '2'"));
    }

    @Test
    public void supportsEquality() throws Exception {
        assertThat(lessThan(2).equals((Object)lessThan(2)), is(true));
        assertThat(lessThan(2).equals((Object)lessThan(3)), is(false));
    }

    @Test
    public void supportsHashCode() throws Exception {
        assertThat(lessThan(2).hashCode(), is(lessThan(2).hashCode()));
        assertThat(lessThan(2).hashCode(), not(lessThan(3).hashCode()));
    }

}
