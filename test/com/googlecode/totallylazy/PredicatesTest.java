package com.googlecode.totallylazy;

import org.junit.Test;

import static com.googlecode.totallylazy.Predicates.assignableTo;
import static com.googlecode.totallylazy.Predicates.setEqualityWith;
import static com.googlecode.totallylazy.Predicates.subsetOf;
import static com.googlecode.totallylazy.Predicates.supersetOf;
import static com.googlecode.totallylazy.Sequences.sequence;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
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

    @Test
    public void logicalAndWithOnePredicateReturnsPredicate() throws Exception {
        Predicate<Object> always = Predicates.always();
        Predicate<Object> predicate = Predicates.and(always);
        assertThat(predicate.matches(null), is(true));
        assertThat(predicate, is(sameInstance(always)));
    }

    @Test
    public void logicalOrWithOnePredicateReturnsPredicate() throws Exception {
        Predicate<Object> always = Predicates.always();
        Predicate<Object> predicate = Predicates.or(always);
        assertThat(predicate.matches(null), is(true));
        assertThat(predicate, is(sameInstance(always)));
    }

    @Test
    public void supportsEqualAsSet() throws Exception {
        assertThat(setEqualityWith(sequence(1, 2, 3)).matches(sequence(1, 2, 3)), is(true));
        assertThat(setEqualityWith(sequence(1, 2, 3)).matches(sequence(3, 1, 2)), is(true));
        assertThat(setEqualityWith(sequence(1, 2, 3)).matches(sequence(1, 2, 3, 4)), is(false));
        assertThat(setEqualityWith(sequence(1, 2, 3)).matches(sequence(1, 2)), is(false));
        assertThat(setEqualityWith(sequence(1, 1)).matches(sequence(1)), is(true));
    }
}

