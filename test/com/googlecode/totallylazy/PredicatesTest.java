package com.googlecode.totallylazy;

import org.junit.Test;

import static com.googlecode.totallylazy.Predicates.assignableTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class PredicatesTest {
    @Test
    public void supportsIsAssignable() throws Exception {
        assertThat(assignableTo(Object.class).matches("aString"), is(true));
        assertThat(assignableTo(SequenceTest.Animal.class).matches(new SequenceTest.Dog()), is(true));
        assertThat(assignableTo(SequenceTest.Cat.class).matches(new SequenceTest.Dog()), is(false));
    }
}

