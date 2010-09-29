package com.googlecode.totallylazy;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

import static com.googlecode.totallylazy.Callables.*;
import static com.googlecode.totallylazy.IterableMatcher.hasExactly;
import static com.googlecode.totallylazy.IterableMatcher.startsWith;
import static com.googlecode.totallylazy.Option.none;
import static com.googlecode.totallylazy.Option.some;
import static com.googlecode.totallylazy.Predicates.*;
import static com.googlecode.totallylazy.Sequences.cons;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Pair.pair;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.hasItems;

public class SequenceTest {
    @Test
    public void supportsCons() throws Exception {
        assertThat(sequence(1, 2, 3).cons(4), hasExactly(4, 1, 2, 3));
        assertThat(cons(4, sequence(1, 2, 3)), hasExactly(4, 1, 2, 3));
    }

    @Test
    public void supportsJoin() throws Exception {
        Sequence<Integer> numbers = sequence(1, 2, 3).join(sequence(4, 5, 6));
        assertThat(numbers, hasExactly(1, 2, 3, 4, 5, 6));
    }

    @Test
    public void supportsAdd() throws Exception {
        Sequence<Integer> numbers = sequence(1, 2, 3).add(4);
        assertThat(numbers, hasExactly(1, 2, 3, 4));
    }


    @Test
    public void supportsTryPick() throws Exception {
        Option<String> converted = sequence(1, 2, 3).tryPick(someVeryExpensiveOperation);
        assertThat(converted, is((Option<String>) some("converted")));
    }

    @Test
    public void supportsPick() throws Exception {
        String converted = sequence(1, 2, 3).pick(someVeryExpensiveOperation);
        assertThat(converted, is("converted"));
    }

    Callable1<Integer, Option<String>> someVeryExpensiveOperation = new Callable1<Integer, Option<String>>() {
        public Option<String> call(Integer integer) throws Exception {
            switch (integer) {
                case 1:
                    return none(); // the conversion didn't work
                case 2:
                    return some("converted"); // the conversion worked so don't do any more
            }
            throw new AssertionError("should never get here");
        }
    };


    @Test
    public void supportsFind() throws Exception {
        assertThat(sequence(1, 3, 5).find(even()), is((Option<Integer>) none(Integer.class)));
        assertThat(sequence(1, 2, 3).find(even()), is((Option<Integer>) some(2)));
    }

    @Test
    public void supportsContains() throws Exception {
        assertThat(sequence(1, 3, 5).contains(2), is(false));
        assertThat(sequence(1, 2, 3).contains(2), is(true));
    }

    @Test
    public void supportsExists() throws Exception {
        assertThat(sequence(1, 3, 5).exists(even()), is(false));
        assertThat(sequence(1, 2, 3).exists(even()), is(true));
    }

    @Test
    public void supportsForAll() throws Exception {
        assertThat(sequence(1, 3, 5).forAll(odd()), is(true));
        assertThat(sequence(1, 2, 3).forAll(odd()), is(false));
    }

    @Test
    public void canFilterNull() throws Exception {
        final Sequence<Integer> numbers = sequence(1, null, 3).filter(notNull(Integer.class));
        assertThat(numbers, hasExactly(1, 3));
    }


    @Test
    public void supportsRemove() throws Exception {
        final Sequence<Integer> numbers = sequence(1, 2, 3).remove(2);
        assertThat(numbers, hasExactly(1, 3));
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
        assertThat(aList, hasExactly(1, 2));
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
        assertThat(sequence(1, 2, 3).toString("(", ", ", ")", 2), is("(1, 2...)"));
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
        assertThat(sequence(1, 2, 3).tail(), hasExactly(2, 3));
    }

    @Test
    public void supportsHead() throws Exception {
        assertThat(sequence(1, 2).head(), is(1));
    }

    @Test
    public void supportsHeadOrOption() throws Exception {
        assertThat(sequence(1).headOption(), is((Option<Integer>) Option.<Integer>some(1)));
        assertThat(Sequences.<Integer>sequence().headOption(), is((Option<Integer>) Option.<Integer>none()));
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
        Iterable<String> strings = sequence(1, 2).map(asString());
        assertThat(strings, hasExactly("1", "2"));
    }

    @Test
    public void mapIsLazy() throws Exception {
        Iterable<Integer> result = sequence(returns(1), callThrows(new Exception(), Integer.class)).
                map(call(Integer.class));
        assertThat(result, startsWith(1));
    }

    @Test
    public void supportsFilter() throws Exception {
        Iterable<Integer> result = sequence(1, 2, 3, 4).filter(even());
        assertThat(result, hasExactly(2, 4));
    }

    @Test
    public void filterIsLazy() throws Exception {
        Iterable<Integer> result = sequence(returns(1), returns(2), callThrows(new Exception(), Integer.class)).
                map(call(Integer.class)).
                filter(even());
        assertThat(result, startsWith(2));
    }

    @Test
    public void supportsFlatMap() throws Exception {
        Iterable<Integer> result = sequence(1, 2, 3).flatMap(new Callable1<Integer, Iterable<Integer>>() {
            public Iterable<Integer> call(Integer value) throws Exception {
                return sequence(value, value * 3);
            }
        });
        assertThat(result, hasExactly(1, 3, 2, 6, 3, 9));
    }

    @Test
    public void supportsTake() throws Exception {
        final Sequence<Integer> sequence = sequence(1, 2, 3).take(2);
        assertThat(sequence, hasExactly(1, 2));
        assertThat(sequence(1).take(2).size(), is(1));
        assertThat(sequence().take(2).size(), is(0));
    }

    @Test
    public void supportsTakeWhile() throws Exception {
        final Sequence<Integer> sequence = sequence(1, 3, 5, 6, 8, 1, 3).takeWhile(odd());
        assertThat(sequence, hasExactly(1, 3, 5));
        assertThat(sequence(1).takeWhile(odd()).size(), is(1));
        assertThat(Sequences.<Integer>sequence().takeWhile(odd()).size(), is(0));
    }

    @Test
    public void supportsDrop() throws Exception {
        final Sequence<Integer> sequence = sequence(1, 2, 3).drop(2);
        assertThat(sequence, hasExactly(3));
        assertThat(sequence(1).drop(2).size(), is(0));
        assertThat(sequence().drop(1).size(), is(0));
    }

    @Test
    public void supportsDropWhile() throws Exception {
        final Sequence<Integer> sequence = sequence(1, 3, 5, 6, 8, 1, 3).dropWhile(odd());
        assertThat(sequence, hasExactly(6, 8, 1, 3));
        assertThat(sequence(1).dropWhile(odd()).size(), is(0));
        assertThat(Sequences.<Integer>sequence().dropWhile(odd()).size(), is(0));
    }

    @Test
    public void supportsZip() {
        final Sequence<Integer> sequence = sequence(1, 3, 5);

        assertThat(sequence.zip(sequence(2, 4, 6, 8)),  hasExactly(pair(1,2), pair(3,4), pair(5,6)));
        assertThat(sequence.zip(sequence(2, 4, 6)),     hasExactly(pair(1,2), pair(3,4), pair(5,6)));
        assertThat(sequence.zip(sequence(2, 4)),        hasExactly(pair(1,2), pair(3,4)));
    }

    @Test
    public void supportsZipWithIndex() {
        assertThat(sequence("Dan", "Matt", "Bob").zipWithIndex(),  hasExactly(pair(0, "Dan"), pair(1,"Matt"), pair(2,"Bob")));
    }
}
