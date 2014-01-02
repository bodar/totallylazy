package com.googlecode.totallylazy;

import org.junit.Test;

import java.io.Serializable;
import java.util.Set;

import static com.googlecode.totallylazy.Predicates.assignableTo;
import static com.googlecode.totallylazy.Predicates.classAssignableTo;
import static com.googlecode.totallylazy.Predicates.in;
import static com.googlecode.totallylazy.Predicates.setEqualityWith;
import static com.googlecode.totallylazy.Predicates.subsetOf;
import static com.googlecode.totallylazy.Predicates.supersetOf;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Sets.set;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;

public class PredicatesTest {
    @Test
    @SuppressWarnings("unchecked")
    public void andWithNoArgumentsIsAlwaysTrue() throws Exception {
        assertThat(Predicates.<Integer>and().matches(1), is(true));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void orWithNoArgumentsIsAlwaysFalse() throws Exception {
        assertThat(Predicates.<Integer>or().matches(1), is(false));
    }

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
    public void supportsClassAssignableToClass() {
        assertThat(classAssignableTo(Serializable.class).matches(Number.class), is(true));
        assertThat(classAssignableTo(SequenceTest.Animal.class).matches(SequenceTest.Dog.class), is(true));
        assertThat(classAssignableTo(SequenceTest.Dog.class).matches(SequenceTest.Cat.class), is(false));
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

    @Test
    public void supportsInCollection() throws Exception {
        Set<Integer> values = set(1, 2, 3);
        assertThat(sequence(2).forAll(in(values)), is(true));
    }
}