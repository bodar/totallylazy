package com.googlecode.totallylazy;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import java.lang.*;
import java.lang.Iterable;
import java.util.List;
import java.util.Set;

import static com.googlecode.totallylazy.Callables.*;
import static com.googlecode.totallylazy.Predicates.notNull;
import static com.googlecode.totallylazy.Predicates.odd;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Predicates.even;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.junit.matchers.JUnitMatchers.hasItem;
import static org.junit.matchers.JUnitMatchers.hasItems;

public class SequenceTest {
    @Test
    public void supportsSize() throws Exception {
        assertThat(sequence(1, 2, 3).size(), is(3));
        assertThat(sequence(1, 2).size(), is(2));
    }

    @Test
    public void canFilterNull() throws Exception {
        final Sequence<Integer> numbers = sequence(1, null, 3).filter(notNull(Integer.class));
        assertThat(numbers, hasItems(1, 3));
    }

    @Test
    public void supportsRemove() throws Exception {
        final Sequence<Integer> numbers = sequence(1, 2, 3).remove(2);
        assertThat(numbers, hasItems(1, 3));
        assertThat(numbers, not(hasItem(2)));
    }

    @Test
    public void canConvertToArray() throws Exception {
        final Integer[] array = sequence(1, 2).toArray(Integer.class);
        assertThat(array[0], is(1));
        assertThat(array[1], is(2));
    }

    @Test
    public void canConvertToList() throws Exception {
        final List<Integer> aList = sequence(1, 2).toList();
        assertThat(aList, hasItems(1, 2));
    }

    @Test
    public void supportsIsEmpty() throws Exception {
        assertThat(sequence().isEmpty(), is(true));
        assertThat(sequence(1).isEmpty(), is(false));
    }

    @Test
    public void supportsUnion() throws Exception {
        Set<Integer> union = sequence(1, 2, 3).union(sequence(5, 4, 3));
        assertThat(union.size(), is(5));
        assertThat(union, hasItems(1, 2, 3, 4, 5));
    }

    @Test
    public void supportsToString() throws Exception {
        assertThat(sequence(1, 2, 3).toString(), is("1,2,3"));
        assertThat(sequence(1, 2, 3).toString(":"), is("1:2:3"));
        assertThat(sequence(1, 2, 3).toString("(", ", ", ")"), is("(1, 2, 3)"));
    }

    @Test
    public void supportsReduceLeft() throws Exception {
        int sum = sequence(1, 2, 3).reduceLeft(add());
        assertThat(sum, is(6));
    }

    @Test
    public void supportsFoldLeft() throws Exception {
        int sum = sequence(1, 2, 3).foldLeft(0, add());
        assertThat(sum, is(6));
    }

    @Test
    public void supportsTail() throws Exception {
        assertThat(sequence(1, 2, 3).tail(), hasItems(2, 3));
    }

    @Test
    public void supportsHead() throws Exception {
        assertThat(sequence(1, 2).head(), is(1));
    }

    @Test
    public void supportsHeadOr() throws Exception {
        assertThat(Sequences.<Integer>sequence().headOr(2), is(2));
        assertThat(Sequences.<Integer>sequence().headOr(returns(2)), is(2));
        assertThat(Sequences.<Integer>sequence().headOr(aNull(Integer.class)), is(nullValue(Integer.class)));
        assertThat(sequence(1, 2).headOr(3), is(1));
    }


    @Test
    public void headOrSupportsExceptions() {
        final Exception expected = new Exception("");
        try {
            assertThat(Sequences.<Integer>sequence().headOr(callThrows(expected, Integer.class)), is(nullValue(Integer.class)));
            fail();
        } catch (LazyException e) {
            assertThat((Exception) e.getCause(), sameInstance(expected));
        }
    }

    @Test
    public void supportsForEach() throws Exception {
        final int[] sum = {0};
        sequence(1, 2).foreach(new Runnable1<Integer>() {
            public void run(Integer value) {
                sum[0] += value;
            }
        });
        assertThat(sum[0], is(3));
    }

    @Test
    public void supportsMap() throws Exception {
        Iterable<String> strings = sequence(1, 2).map(asString(Integer.class));
        assertThat(strings, hasItems("1", "2"));
    }

    @Test
    public void mapIsLazy() throws Exception {
        Iterable<Integer> result = sequence(returns(1), callThrows(new Exception(), Integer.class)).
                map(invoke(Integer.class));
        assertThat(result, hasItem(1));
    }

    @Test
    public void supportsFilter() throws Exception {
        Iterable<Integer> result = sequence(1, 2, 3, 4).filter(even());
        assertThat(result, hasItems(2, 4));
    }

    @Test
    public void filterIsLazy() throws Exception {
        Iterable<Integer> result = sequence(returns(1), returns(2), callThrows(new Exception(), Integer.class)).
                map(invoke(Integer.class)).
                filter(even());
        assertThat(result, hasItem(2));
    }

    @Test
    public void supportsFlatMap() throws Exception {
        Iterable<Integer> result = sequence(1, 2, 3).flatMap(new Callable1<Integer, Iterable<Integer>>() {
            public Iterable<Integer> call(Integer value) throws Exception {
                return sequence(value, value * 3);
            }
        });
        assertThat(result, hasItems(1, 2, 3, 6, 9));
    }

    @Test
    public void supportsTake() throws Exception {
        final Sequence<Integer> sequence = sequence(1, 2, 3).take(2);
        assertThat(sequence, hasItems(1, 2));
        assertThat(sequence.size(), is(2));
        assertThat(sequence(1).take(2).size(), is(1));
        assertThat(sequence().take(2).size(), is(0));
    }

    @Test
    public void supportsTakeWhile() throws Exception {
        final Sequence<Integer> sequence = sequence(1, 3, 5, 6, 8).takeWhile(odd());
        assertThat(sequence, hasItems(1, 3, 5));
        assertThat(sequence.size(), is(3));
        assertThat(sequence(1).takeWhile(odd()).size(), is(1));
        assertThat(Sequences.<Integer>sequence().takeWhile(odd()).size(), is(0));
    }

    @Test
    public void supportsDrop() throws Exception {
        final Sequence<Integer> sequence = sequence(1, 2, 3).drop(2);
        assertThat(sequence, hasItems(3));
        assertThat(sequence.size(), is(1));
        assertThat(sequence(1).drop(2).size(), is(0));
        assertThat(sequence().drop(1).size(), is(0));
    }

    @Test
    public void supportsDropWhile() throws Exception {
        final Sequence<Integer> sequence = sequence(1, 3, 5, 6, 8, 1, 3).dropWhile(odd());
        assertThat(sequence, hasItems(6, 8, 1, 3));
        assertThat(sequence.size(), is(4));
        assertThat(sequence(1).dropWhile(odd()).size(), is(0));
        assertThat(Sequences.<Integer>sequence().dropWhile(odd()).size(), is(0));
    }

}
