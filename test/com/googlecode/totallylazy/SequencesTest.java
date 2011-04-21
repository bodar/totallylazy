package com.googlecode.totallylazy;

import org.junit.Test;

import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

import static com.googlecode.totallylazy.Callables.returns;
import static com.googlecode.totallylazy.Predicates.notNull;
import static com.googlecode.totallylazy.Sequences.characters;
import static com.googlecode.totallylazy.Sequences.iterate;
import static com.googlecode.totallylazy.Sequences.join;
import static com.googlecode.totallylazy.Sequences.range;
import static com.googlecode.totallylazy.Sequences.repeat;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.matchers.IterableMatcher.hasExactly;
import static com.googlecode.totallylazy.matchers.IterableMatcher.startsWith;
import static com.googlecode.totallylazy.numbers.Numbers.even;
import static com.googlecode.totallylazy.numbers.Numbers.increment;
import static com.googlecode.totallylazy.numbers.Numbers.odd;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;

public class SequencesTest {
    @Test
    public void supportsAddingToAnEmptyList() throws Exception {
        assertThat(sequence().add(1).add(2).add(3), hasExactly(1,2,3));
    }

    @Test
    public void joinWorksEvenWhenFirstIterableIsEmpty() throws Exception {
        final List<Integer> emptyList = Collections.<Integer>emptyList();
        assertThat(join(emptyList, asList(1, 2, 3)), hasExactly(1,2,3));
        assertThat(join(emptyList, asList(1, 2, 3), emptyList, asList(4, 5, 6)), hasExactly(1,2,3,4,5,6));
    }

    @Test
    public void supportsEnumeration() throws Exception {
        Vector<String> vector = new Vector<String>();
        vector.add("foo");
        Enumeration enumeration = vector.elements();
        assertThat(sequence(enumeration, String.class).head(), is("foo"));
        Enumeration<String> typeSafeEnumeration = vector.elements();
        assertThat(sequence(typeSafeEnumeration).head(), is("foo"));
    }

    @Test
    public void supportRepeat() throws Exception {
        assertThat(repeat(10), startsWith(10, 10, 10, 10, 10));
        assertThat(repeat(returns(10)), startsWith(10, 10, 10, 10, 10));
    }

    @Test
    public void supportsCharacters() throws Exception {
        assertThat(characters("text"), hasExactly('t', 'e', 'x', 't'));
        assertThat(characters("text".toCharArray()), hasExactly('t', 'e', 'x', 't'));
        assertThat(characters("text").drop(2).toString(""), is("xt"));
    }

    @Test
    public void supportsRange() throws Exception {
        assertThat(range(5), hasExactly(0, 1, 2, 3, 4));
        assertThat(range(0, 5), hasExactly(0, 1, 2, 3, 4));
        assertThat(range(0, 5, 2), hasExactly(0, 2, 4));
    }

    @Test
    public void supportsIterate() throws Exception {
        assertThat(iterate(increment(), 1), startsWith(1, 2, 3, 4, 5));
    }

    @Test
    public void supportsIteratingEvenWhenCallableReturnNull() throws Exception {
        final Sequence<Integer> sequence = iterate(new Callable1<Integer, Integer>() {
            public Integer call(Integer integer) throws Exception {
                assertThat("Should never see a null value", integer, is(notNullValue()));
                return null;
            }
        }, 1).takeWhile(notNull(Integer.class));
        assertThat(sequence, hasExactly(1));
    }



    @Test
    public void canCombineIterateWithOtherOperations() throws Exception {
        final Sequence<Number> numbers = iterate(increment(), 1);
        assertThat(numbers.filter(even()), startsWith(2, 4, 6));
        assertThat(numbers.filter(odd()), startsWith(1, 3, 5, 7, 9));
    }
}
