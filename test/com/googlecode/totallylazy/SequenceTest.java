package com.googlecode.totallylazy;

import com.googlecode.totallylazy.callables.CountingCallable;
import com.googlecode.totallylazy.callables.SleepyCallable;
import com.googlecode.totallylazy.callables.TimeCallable;
import com.googlecode.totallylazy.matchers.NumberMatcher;
import com.googlecode.totallylazy.numbers.Numbers;
import org.junit.Test;

import java.util.List;

import static com.googlecode.totallylazy.Arrays.list;
import static com.googlecode.totallylazy.Callables.*;
import static com.googlecode.totallylazy.Callables.length;
import static com.googlecode.totallylazy.Option.none;
import static com.googlecode.totallylazy.Option.some;
import static com.googlecode.totallylazy.Option.option;
import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.Predicates.notNullValue;
import static com.googlecode.totallylazy.callables.CountingCallable.counting;
import static com.googlecode.totallylazy.callables.SleepyCallable1.sleepy;
import static com.googlecode.totallylazy.matchers.IterableMatcher.hasExactly;
import static com.googlecode.totallylazy.matchers.IterableMatcher.startsWith;
import static com.googlecode.totallylazy.Sequences.*;
import static com.googlecode.totallylazy.numbers.Numbers.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class SequenceTest {
    @Test
    public void supportsPartition() throws Exception {
        Partition<Integer> result = sequence(1, 2, 3, 4).partition(even());
        assertThat(result.matched().realise(), hasExactly(2, 4));
        assertThat(result.unmatched().realise(), hasExactly(1, 3));
    }

    @Test
    public void supportsPartitionOnForwardOnlySequence() throws Exception {
        Partition<Integer> result = sequence(1, 2, 3, 4).forwardOnly().partition(even());
        assertThat(result.matched().realise(), hasExactly(2, 4));
        assertThat(result.unmatched().realise(), hasExactly(1, 3));
    }

    @Test
    public void supportsLast() throws Exception {
        assertThat(sequence(1,2,3).last(), is(3));
    }

    @Test
    public void supportsReverse() throws Exception {
        assertThat(sequence(1,2,3).reverse(), hasExactly(3,2,1));
    }

    @Test
    public void supportsSize() throws Exception {
        assertThat(range(10000000000L, 10000000100L).size(), NumberMatcher.is(100));
    }

    @Test
    public void canRealiseASequence() throws Exception {
        CountingCallable<Integer> counting = counting();
        Sequence<Integer> lazy = sequence(counting).map(call(Integer.class));
        assertThat(counting.count(), is(0));
        assertThat(lazy, hasExactly(0)); // this will increment count by 1
        Sequence<Integer> realised = lazy.realise(); // this will increment count by 1
        assertThat(counting.count(), is(2));
        assertThat(realised, hasExactly(1));
        assertThat(realised, hasExactly(1));
    }

    @Test
    public void supportsSafeCast() throws Exception {
        Cat freaky = new Cat(), fatty = new Cat();
        Dog buster = new Dog();
        Sequence<Animal> animals = sequence(freaky, fatty, buster);
        Sequence<Cat> cats = animals.safeCast(Cat.class);
        Sequence<Dog> dogs = animals.safeCast(Dog.class);
        assertThat(cats, hasExactly(freaky, fatty));
        assertThat(dogs, hasExactly(buster));
    }

    public static interface Animal {}

    public static class Cat implements Animal {}

    public static class Dog implements Animal {}

    @Test
    public void supportsSort() throws Exception {
        assertThat(sort(sequence(5, 6, 1, 3, 4, 2)), hasExactly(1, 2, 3, 4, 5, 6));
        assertThat(sort(sequence("Matt", "Dan", "Bob")), hasExactly("Bob", "Dan", "Matt"));
    }

    @Test
    public void supportsSortBy() throws Exception {
        int[] small = {1};
        int[] medium = {1, 2, 3};
        int[] large = {1, 2, 3, 4, 5, 6};
        Sequence<int[]> unsorted = sequence(large, small, medium);
        assertThat(unsorted.sortBy(length()), hasExactly(small, medium, large));
        assertThat(unsorted.sortBy(ascending(length())), hasExactly(small, medium, large));
        assertThat(unsorted.sortBy(descending(length())), hasExactly(large, medium, small));
    }

    @Test
    public void supportsSortBySizeAndLength() throws Exception {
        List<Integer> small = list(1);
        int[] medium = {1, 2, 3};
        List<Integer> large = list(1, 2, 3, 4, 5, 6);
        Sequence<Object> unsorted = sequence(large, small, medium);
        assertThat(unsorted.sortBy(size()), hasExactly(small, medium, large));
        assertThat(unsorted.sortBy(ascending(size())), hasExactly(small, medium, large));
        assertThat(unsorted.sortBy(descending(length())), hasExactly(large, medium, small));
    }

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
        public Option<String> call(Integer number) throws Exception {
            if(Numbers.equalTo(number, 1)) {
                return none(); // the conversion didn't work
            }
            if(Numbers.equalTo(number, 2)) {
                    return some("converted"); // the conversion worked so don't do any more
            }
            throw new AssertionError("should never get here");
        }
    };

    @Test
    public void supportsFind() throws Exception {
        assertThat(sequence(1, 3, 5).find(even()), is((Option<Integer>) none(Integer.class)));
        assertThat(sequence(1, 2, 3).find(even()), is((Option<Integer>) some(2)));
        assertThat(sequence(none(Integer.class), some(2), some(3)).find(Predicates.<Integer>some()).get(), is((Option<Integer>) some(2)));
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
        final Sequence<Integer> numbers = sequence(1, null, 3).filter(notNullValue());
        assertThat(numbers, hasExactly(1, 3));
    }

    @Test
    public void supportsRemove() throws Exception {
        final Sequence<Integer> numbers = sequence(1, 2, 3, 2).remove(2);
        assertThat(numbers, hasExactly(1, 3, 2));
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
    public void supportsToString() throws Exception {
        assertThat(sequence(1, 2, 3).toString(), is("1,2,3"));
        assertThat(sequence(1, 2, 3).toString(":"), is("1:2:3"));
        assertThat(sequence(1, 2, 3).toString("(", ", ", ")"), is("(1, 2, 3)"));
        assertThat(sequence(1, 2, 3).toString("(", ", ", ")", 2), is("(1, 2...)"));
    }

    @Test
    public void supportsReduceLeft() throws Exception {
        Number sum = numbers(1, 2, 3).reduceLeft(add());
        assertThat(sum, NumberMatcher.is(6));
    }

    @Test
    public void supportsFoldLeft() throws Exception {
        Number sum = sequence(1, 2, 3).foldLeft(0, add());
        assertThat(sum, NumberMatcher.is(6));
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
        assertThat(Sequences.<Number>sequence().headOption(), is((Option<Number>) Option.<Number>none()));
    }


    @Test
    public void supportsForEach() throws Exception {
        final int[] sum = {0};
        sequence(1, 2).forEach(new Callable1<Integer, Void>() {
            public Void call(Integer value) {
                sum[0] += value;
                return Runnables.VOID;

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
    public void supportsConcurrentMap() throws Exception {
        Iterable<String> strings = sequence(1, 2).mapConcurrently(asString());
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
        assertThat(sequence(1).take(2).size(), NumberMatcher.is(1));
        assertThat(sequence().take(2).size(), NumberMatcher.is(0));
    }

    @Test
    public void supportsTakeWhile() throws Exception {
        final Sequence<Integer> sequence = sequence(1, 3, 5, 6, 8, 1, 3).takeWhile(odd());
        assertThat(sequence, hasExactly(1, 3, 5));
        assertThat(sequence(1).takeWhile(odd()).size(), NumberMatcher.is(1));
        assertThat(Sequences.<Number>sequence().takeWhile(odd()).size(), NumberMatcher.is(0));
    }

    @Test
    public void supportsDrop() throws Exception {
        final Sequence<Integer> sequence = sequence(1, 2, 3).drop(2);
        assertThat(sequence, hasExactly(3));
        assertThat(sequence(1).drop(2).size(), NumberMatcher.is(0));
        assertThat(sequence().drop(1).size(), NumberMatcher.is(0));
    }

    @Test
    public void supportsDropWhile() throws Exception {
        final Sequence<Integer> sequence = sequence(1, 3, 5, 6, 8, 1, 3).dropWhile(odd());
        assertThat(sequence, hasExactly(6, 8, 1, 3));
        assertThat(sequence(1).dropWhile(odd()).size(), NumberMatcher.is(0));
        assertThat(Sequences.<Number>sequence().dropWhile(odd()).size(), NumberMatcher.is(0));
    }

    @Test
    public void supportsZip() {
        final Sequence<Integer> sequence = sequence(1, 3, 5);

        assertThat(sequence.zip(sequence(2, 4, 6, 8)), hasExactly(pair(1, 2), pair(3, 4), pair(5, 6)));
        assertThat(sequence.zip(sequence(2, 4, 6)), hasExactly(pair(1, 2), pair(3, 4), pair(5, 6)));
        assertThat(sequence.zip(sequence(2, 4)), hasExactly(pair(1, 2), pair(3, 4)));
    }

    @Test
    public void supportsZipWithIndex() {
        assertThat(sequence("Dan", "Matt", "Bob").zipWithIndex(), hasExactly(pair((Number)0, "Dan"), pair((Number)1, "Matt"), pair((Number)2, "Bob")));
    }


    @Test
    public void supportsForwardOnly() throws Exception {
        Sequence<Integer> sequence = sequence(1, 2, 3, 4).forwardOnly();

        assertThat(sequence.headOption(), is(option(1)));
        assertThat(sequence.headOption(), is(option(2)));
    }
}
