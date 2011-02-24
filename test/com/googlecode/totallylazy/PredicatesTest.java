package com.googlecode.totallylazy;

import org.junit.Test;

import static com.googlecode.totallylazy.Predicates.assignableTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class PredicatesTest {
    @Test
    public void supportsClassAssignableTo() throws Exception {
        assertThat(assignableTo(Object.class).matches("aString"), is(true));
        assertThat(assignableTo(SequenceTest.Animal.class).matches(new SequenceTest.Dog()), is(true));
        assertThat(assignableTo(SequenceTest.Cat.class).matches(new SequenceTest.Dog()), is(false));
    }

    @Test
    public void supportsObjectAssignableTo() throws Exception {
        assertThat(assignableTo("aString").matches(String.class), is(true));
        assertThat(assignableTo(new SequenceTest.Dog()).matches(SequenceTest.Animal.class), is(true));
        assertThat(assignableTo(new SequenceTest.Dog()).matches(SequenceTest.Cat.class), is(false));
    }
}

