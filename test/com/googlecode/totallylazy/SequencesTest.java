package com.googlecode.totallylazy;

import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.Enumeration;
import java.util.Vector;

import static com.googlecode.totallylazy.Callables.returns;
import static com.googlecode.totallylazy.Option.none;
import static com.googlecode.totallylazy.Option.some;
import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.Quadruple.quadruple;
import static com.googlecode.totallylazy.Quintuple.quintuple;
import static com.googlecode.totallylazy.Sequences.characters;
import static com.googlecode.totallylazy.Sequences.empty;
import static com.googlecode.totallylazy.Sequences.iterate;
import static com.googlecode.totallylazy.Sequences.join;
import static com.googlecode.totallylazy.Sequences.repeat;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Sequences.transpose;
import static com.googlecode.totallylazy.Triple.triple;
import static com.googlecode.totallylazy.matchers.IterableMatcher.hasExactly;
import static com.googlecode.totallylazy.matchers.IterableMatcher.startsWith;
import static com.googlecode.totallylazy.numbers.Numbers.decrement;
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
    public void shouldCreateEmptySequenceWhenIterableIsNull() throws Exception {
        assertThat(sequence((Iterable<Object>) null), is(empty()));
        assertThat(sequence((Object[]) null), is(empty(Object.class)));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void supportsTranspose() {
        Sequence<Sequence<Integer>> transposed = transpose(sequence(1, 2), sequence(3, 4), sequence(5, 6));
        assertThat(transposed, hasExactly(sequence(1, 3, 5), sequence(2, 4, 6)));
        assertThat(transpose(transposed), hasExactly(sequence(1, 2), sequence(3, 4), sequence(5, 6)));
        assertThat(sequence(1, 2).transpose(sequence(3, 4)), hasExactly(sequence(1, 3), sequence(2, 4)));
    }

    @Test
    public void supportsUnzip() {
        Pair<Sequence<Integer>, Sequence<Integer>> pair = Sequences.unzip(sequence(pair(1, 2), pair(3, 4), pair(5, 6)));
        assertThat(pair.first(), hasExactly(1, 3, 5));
        assertThat(pair.second(), hasExactly(2, 4, 6));
    }

    @Test
    public void supportsUnzippingTriples() {
        Triple<Sequence<Integer>, Sequence<Integer>, Sequence<String>> triple = Sequences.unzip3(sequence(triple(1, 2, "car"), triple(3, 4, "cat")));
        assertThat(triple.first(), hasExactly(1, 3));
        assertThat(triple.second(), hasExactly(2, 4));
        assertThat(triple.third(), hasExactly("car", "cat"));
    }

    @Test
    public void supportsUnzippingQuadruples() {
        Quadruple<Sequence<Integer>, Sequence<Integer>, Sequence<String>, Sequence<Character>> quadruple = Sequences.unzip4(sequence(quadruple(1, 2, "car", 'C'), quadruple(3, 4, "cat", 'D')));
        assertThat(quadruple.first(), hasExactly(1, 3));
        assertThat(quadruple.second(), hasExactly(2, 4));
        assertThat(quadruple.third(), hasExactly("car", "cat"));
        assertThat(quadruple.fourth(), hasExactly('C', 'D'));
    }

    @Test
    public void supportsUnzippingQuintuples() {
        Quintuple<Sequence<Integer>, Sequence<Integer>, Sequence<String>, Sequence<Character>, Sequence<Integer>> quintuple =
                Sequences.unzip5(sequence(quintuple(1, 2, "car", 'C', 1), quintuple(3, 4, "cat", 'D', 2)));
        assertThat(quintuple.first(), hasExactly(1, 3));
        assertThat(quintuple.second(), hasExactly(2, 4));
        assertThat(quintuple.third(), hasExactly("car", "cat"));
        assertThat(quintuple.fourth(), hasExactly('C', 'D'));
        assertThat(quintuple.fifth(), hasExactly(1, 2));
    }

    @Test
    public void supportsCycle() throws Exception {
        assertThat(range(1, 3).cycle(), startsWith((Number) 1, 2, 3, 1, 2, 3, 1, 2, 3, 1, 2, 3));
    }

    @Test
    public void supportsAddingToAnEmptyList() throws Exception {
        assertThat(sequence().append(1).append(2).append(3), hasExactly((Object) 1, 2, 3));
    }

    @Test
    public void joinWorksEvenWhenFirstIterableIsEmpty() throws Exception {
        final Sequence<Integer> empty = Sequences.<Integer>empty();
        assertThat(empty.append(1).join(sequence(2, 3)).append(4), hasExactly(1, 2, 3, 4));
        assertThat(join(empty, sequence(1, 2, 3), empty, asList(4, 5, 6)), hasExactly(1, 2, 3, 4, 5, 6));
    }

    @Test
    public void supportsJoiningSubTypes() throws Exception {
        final Sequence<Number> numbers = numbers(2, 3.0D);
        Sequence<Integer> integers = sequence(2, 3);
        Sequence<Long> longs = sequence(2L, 3L);
        assertThat(numbers.join(integers).join(longs), hasExactly((Number) 2, 3.0D, 2, 3, 2L, 3L));
        assertThat(join(sequence(1L, 2.0D, 3), numbers, asList(4, 5, 6), integers), hasExactly((Number) 1L, 2.0D, 3, 2, 3.0D, 4, 5, 6, 2, 3));
    }

    @Test
    public void supportsEnumeration() throws Exception {
        Vector<String> vector = new Vector<String>();
        vector.add("foo");
        Enumeration enumeration = vector.elements();
        Sequence<String> forwardOnly = Sequences.forwardOnly(enumeration, String.class);
        assertThat(forwardOnly.headOption().isEmpty(), is(false));
        assertThat(forwardOnly.headOption().isEmpty(), is(true));
        Sequence<String> forwardOnlyWithType = Sequences.forwardOnly(vector.elements());
        assertThat(forwardOnlyWithType.headOption().isEmpty(), is(false));
        assertThat(forwardOnlyWithType.headOption().isEmpty(), is(true));
    }

    @Test
    public void supportsMemorisingAnEnumeration() throws Exception {
        Vector<String> vector = new Vector<String>();
        vector.add("foo");
        Enumeration enumeration = vector.elements();
        Sequence<String> memorise = Sequences.memorise(enumeration, String.class);
        assertThat(memorise.headOption().isEmpty(), is(false));
        assertThat(memorise.headOption().isEmpty(), is(false));
        Sequence<String> memorisedWithType = Sequences.memorise(vector.elements());
        assertThat(memorisedWithType.headOption().isEmpty(), is(false));
        assertThat(memorisedWithType.headOption().isEmpty(), is(false));
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
        assertThat(iterate(increment, 1), startsWith((Number) 1, 2, 3, 4, 5));
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
        final Sequence<Number> numbers = iterate(increment, 1);
        assertThat(numbers.filter(even()), startsWith((Number) 2, 4, 6));
        assertThat(numbers.filter(odd()), startsWith((Number) 1, 3, 5, 7, 9));
    }

    @Test
    public void supportsUnfoldRight() throws Exception {
        Sequence<Number> result = Sequences.unfoldRight(new Function1<Number, Option<Pair<Number, Number>>>() {
            @Override
            public Option<Pair<Number, Number>> call(Number number) throws Exception {
                if (number.equals(0)) return none();
                return some(pair(number, decrement(number)));
            }
        }, 10);
        assertThat(result, is(range(10, 1)));
    }
}
