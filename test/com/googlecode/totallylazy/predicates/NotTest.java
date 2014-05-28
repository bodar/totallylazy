package com.googlecode.totallylazy.predicates;

import com.googlecode.totallylazy.Predicates;
import org.junit.Test;

import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Assert.assertThat;

public class NotTest {

    @Test
    public void supportsEqualOfThePredicateItself() throws Exception {
        assertThat(Not.not(Predicates.is(2)).equals(Not.not(Predicates.is(2))), is(true));
        assertThat(Not.not(Predicates.is(2)).equals(Not.not(Predicates.is(3))), is(false));
    }

    @Test
    public void supportsToString() throws Exception {
        assertThat(Not.not(Predicates.is(5)).toString(), is("not 5"));
    }

}
