package com.googlecode.totallylazy;

import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.Enumeration;
import java.util.Vector;

import static com.googlecode.totallylazy.Callables.returns;
import static com.googlecode.totallylazy.Sequences.characters;
import static com.googlecode.totallylazy.Sequences.iterate;
import static com.googlecode.totallylazy.Sequences.join;
import static com.googlecode.totallylazy.Sequences.repeat;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.matchers.IterableMatcher.hasExactly;
import static com.googlecode.totallylazy.matchers.IterableMatcher.startsWith;
import static com.googlecode.totallylazy.numbers.Numbers.even;
import static com.googlecode.totallylazy.numbers.Numbers.increment;
import static com.googlecode.totallylazy.numbers.Numbers.numbers;
import static com.googlecode.totallylazy.numbers.Numbers.odd;
import static com.googlecode.totallylazy.numbers.Numbers.range;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class SequencesTest {
    @Test
    public void supportsCycle() throws Exception {
        assertThat(range(1, 4).cycle(), startsWith(1, 2, 3, 1, 2, 3, 1, 2, 3, 1, 2, 3));
    }

    @Test
    public void supportsAddingToAnEmptyList() throws Exception {
        assertThat(sequence().add(1).add(2).add(3), hasExactly(1, 2, 3));
    }

    @Test
    public void joinWorksEvenWhenFirstIterableIsEmpty() throws Exception {
        final Sequence<Integer> empty = Sequences.<Integer>empty();
        assertThat(empty.add(1).join(sequence(2, 3)).add(4), hasExactly(1, 2, 3, 4));
        assertThat(join(empty, sequence(1, 2, 3), empty, asList(4, 5, 6)), hasExactly(1, 2, 3, 4, 5, 6));
    }

    @Test
    public void supportsJoiningSubTypes() throws Exception {
        final Sequence<Number> numbers = numbers(2, 3.0D);
        Sequence<Integer> integers = sequence(2, 3);
        Sequence<Long> longs = sequence(2L, 3L);
        assertThat(numbers.join(integers).join(longs), hasExactly(2, 3.0D, 2, 3, 2L, 3L));
        assertThat(join(sequence(1L, 2.0D, 3), numbers, asList(4, 5, 6), integers), hasExactly(1L, 2.0D, 3, 2, 3.0D, 4, 5, 6, 2, 3));
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
    public void supportsIterate() throws Exception {
        assertThat(iterate(increment(), 1), startsWith(1, 2, 3, 4, 5));
    }

    @Test
    public void supportsIteratingEvenWhenCallableReturnNull() throws Exception {
        final Sequence<Integer> sequence = iterate(new Callable1<Integer, Integer>() {
            public Integer call(Integer integer) throws Exception {
                assertThat("Should never see a null value", integer, is(Matchers.notNullValue()));
                return null;
            }
        }, 1).takeWhile(Predicates.notNullValue());
        assertThat(sequence, hasExactly(1));
    }


    @Test
    public void canCombineIterateWithOtherOperations() throws Exception {
        final Sequence<Number> numbers = iterate(increment(), 1);
        assertThat(numbers.filter(even()), startsWith(2, 4, 6));
        assertThat(numbers.filter(odd()), startsWith(1, 3, 5, 7, 9));
    }
}
