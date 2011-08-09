package com.googlecode.totallylazy;

import org.junit.Test;

import static com.googlecode.totallylazy.Predicates.assignableTo;
import static com.googlecode.totallylazy.Predicates.subsetOf;
import static com.googlecode.totallylazy.Predicates.supersetOf;
import static com.googlecode.totallylazy.Sequences.sequence;
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

    @Test
    public void supportsSubsetOf() throws Exception {
        assertThat(subsetOf(sequence("a", "b")).matches(sequence("a")), is(true));
        assertThat(subsetOf(sequence("a")).matches(sequence("a", "b")), is(false));
        assertThat(subsetOf(sequence("a")).matches(Sequences.<String>sequence()), is(true));
    }

    @Test
    public void supportsSupersetOf() throws Exception {
        assertThat(supersetOf(sequence("a")).matches(sequence("a", "b")), is(true));
        assertThat(supersetOf(sequence("a", "b")).matches(sequence("a")), is(false));
        assertThat(supersetOf(Sequences.<String>sequence()).matches(sequence("a")), is(true));
    }
}

