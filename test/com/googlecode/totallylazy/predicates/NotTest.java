package com.googlecode.totallylazy.predicates;

import com.googlecode.totallylazy.Predicates;
import org.junit.Test;

import static com.googlecode.totallylazy.matchers.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class NotTest {

    @Test
    public void supportsEqualOfThePredicateItself() throws Exception {
        assertThat(new Not<Integer>(Predicates.is(2)).equals(new Not<Integer>(Predicates.is(2))), is(true));
        assertThat(new Not<Integer>(Predicates.is(2)).equals(new Not<Integer>(Predicates.is(3))), is(false));
    }

    @Test
    public void supportsToString() throws Exception {
        assertThat(new Not<Integer>(Predicates.is(5)).toString(), is("not is 5"));
    }

}
