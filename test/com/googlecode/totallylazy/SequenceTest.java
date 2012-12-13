package com.googlecode.totallylazy;

import com.googlecode.totallylazy.callables.CountingCallable;
import com.googlecode.totallylazy.comparators.Comparators;
import com.googlecode.totallylazy.matchers.Matchers;
import com.googlecode.totallylazy.matchers.NumberMatcher;
import com.googlecode.totallylazy.numbers.Numbers;
import com.googlecode.totallylazy.time.Dates;
import com.googlecode.yatspec.junit.Notes;
import com.googlecode.yatspec.junit.SpecRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.googlecode.totallylazy.Arrays.list;
import static com.googlecode.totallylazy.Callables.ascending;
import static com.googlecode.totallylazy.Callables.call;
import static com.googlecode.totallylazy.Callables.callThrows;
import static com.googlecode.totallylazy.Callables.descending;
import static com.googlecode.totallylazy.Callables.length;
import static com.googlecode.totallylazy.Callables.returnArgument;
import static com.googlecode.totallylazy.Callables.returns;
import static com.googlecode.totallylazy.Callables.second;
import static com.googlecode.totallylazy.Callables.size;
import static com.googlecode.totallylazy.Callables.toString;
import static com.googlecode.totallylazy.Functions.and;
import static com.googlecode.totallylazy.Functions.andPair;
import static com.googlecode.totallylazy.Functions.or;
import static com.googlecode.totallylazy.Functions.orPair;
import static com.googlecode.totallylazy.Functions.xor;
import static com.googlecode.totallylazy.Lists.indexIn;
import static com.googlecode.totallylazy.Option.identity;
import static com.googlecode.totallylazy.Option.none;
import static com.googlecode.totallylazy.Option.option;
import static com.googlecode.totallylazy.Option.some;
import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.Predicates.greaterThan;
import static com.googlecode.totallylazy.Predicates.lessThan;
import static com.googlecode.totallylazy.Predicates.notNullValue;
import static com.googlecode.totallylazy.Quadruple.quadruple;
import static com.googlecode.totallylazy.Quintuple.quintuple;
import static com.googlecode.totallylazy.Sequences.applicate;
import static com.googlecode.totallylazy.Sequences.characters;
import static com.googlecode.totallylazy.Sequences.cons;
import static com.googlecode.totallylazy.Sequences.empty;
import static com.googlecode.totallylazy.Sequences.flatOption;
import static com.googlecode.totallylazy.Sequences.one;
import static com.googlecode.totallylazy.Sequences.repeat;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Sequences.sort;
import static com.googlecode.totallylazy.Sequences.splitOn;
import static com.googlecode.totallylazy.Sequences.zip;
import static com.googlecode.totallylazy.Strings.toCharacters;
import static com.googlecode.totallylazy.Triple.triple;
import static com.googlecode.totallylazy.callables.Count.count;
import static com.googlecode.totallylazy.callables.CountingCallable.counting;
import static com.googlecode.totallylazy.comparators.Comparators.comparators;
import static com.googlecode.totallylazy.matchers.IterableMatcher.hasExactly;
import static com.googlecode.totallylazy.matchers.IterableMatcher.isEmpty;
import static com.googlecode.totallylazy.matchers.IterableMatcher.startsWith;
import static com.googlecode.totallylazy.numbers.Numbers.add;
import static com.googlecode.totallylazy.numbers.Numbers.even;
import static com.googlecode.totallylazy.numbers.Numbers.multiply;
import static com.googlecode.totallylazy.numbers.Numbers.numbers;
import static com.googlecode.totallylazy.numbers.Numbers.odd;
import static com.googlecode.totallylazy.numbers.Numbers.range;
import static com.googlecode.totallylazy.numbers.Numbers.sum;
import static java.lang.Thread.currentThread;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.fail;

@RunWith(SpecRunner.class)
public class SequenceTest {
    @Test
    @Notes("Eagerly return the first element of a sequence, throws NoSuchElementException if empty.")
    public void head() throws Exception {
        assertThat(sequence(1, 2).head(), is(1));
    }

    @Test
    @Notes("Eagerly return the first element of a sequence wrapped in a some, returns none if empty.")
    public void headOption() throws Exception {
        assertThat(sequence(1).headOption(), is(some(1)));
        assertThat(empty().headOption(), is(none()));
    }

    @Test
    @Notes("Eagerly return the last element of a finite sequence, throws NoSuchElementException if empty.")
    public void last() throws Exception {
        assertThat(sequence(1, 2, 3).last(), is(3));
    }

    @Test
    @Notes("Eagerly return the last element of a finite sequence wrapped in a some, returns none if empty.")
    public void lastOption() throws Exception {
        assertThat(sequence(1, 2, 3).lastOption(), is(some(3)));
        assertThat(empty().lastOption(), is(none()));
    }

    @Test
    @Notes("Lazily returns the elements after the head of the sequence. Lazily throws NoSuchElementException if empty. Works with infinite sequences.")
    public void tail() throws Exception {
        assertThat(sequence(1, 2, 3).tail(), hasExactly(2, 3));
        assertThat(sequence(1).tail().isEmpty(), is(true));
        try {
            empty().tail().isEmpty();
            fail("Should have thrown NoSuchElementException");
        } catch (NoSuchElementException e) {
            // all good
        }
    }

    @Test
    @Notes("Lazily returns all the elements of a finite sequence except the last one. Lazily throws NoSuchElementException if empty.s")
    public void init() throws Exception {
        assertThat(sequence(1, 2, 3).init(), hasExactly(1, 2));
        assertThat(sequence(1).init().isEmpty(), is(true));
        try {
            empty().init().isEmpty();
            fail("Should have thrown NoSuchElementException");
        } catch (NoSuchElementException e) {
            // all good
        }
    }

    @Test
    @Notes("This test has a very small chance that it could fail")
    public void supportsShuffle() throws Exception {
        assertThat(range(1, 100).shuffle(), is(not(range(1, 100))));
    }

    @Test
    public void supportsReduceWithIdentityFunction() throws Exception {
        assertThat(sequence("one", "two", "three").reduce(count()), NumberMatcher.is(3));
    }

    @Test
    public void supportsApplicativeUsage() throws Exception {
        assertThat(empty(Number.class).applicate(one(add(3))), Matchers.is(empty(Number.class)));
        assertThat(numbers(9).applicate(Sequences.<Function1<Number, Number>>empty()), Matchers.is(empty(Number.class)));
        assertThat(numbers(9).applicate(one(add(3))), Matchers.is(numbers(12)));
        assertThat(numbers(9, 1).applicate(one(add(3))), Matchers.is(numbers(12, 4)));
        assertThat(numbers(9, 1).applicate(sequence(add(3), multiply(10))), Matchers.is(numbers(12, 4, 90, 10)));

        //http://learnyouahaskell.com/functors-applicative-functors-and-monoids#applicative-functors (Lists)
        assertThat(applicate(applicate(sequence(add(), multiply()), numbers(1, 2)), numbers(3, 4)), Matchers.is(numbers(4, 5, 5, 6, 3, 4, 6, 8)));
    }

    @Test
    public void recursiveCallOnlyEndsWhenThereIsNoRemainder() throws Exception {
        assertThat(sequence(1, 3, 0, 0, 2).recursive(splitOn(0)),
                is(sequence(sequence(1, 3), empty(Integer.class), sequence(2))));
    }

    @Test
    public void supportsRecursiveSplitOn() throws Exception {
        assertThat(sequence(1, 3, -4, 0, 7, -9, 0, 2).recursive(splitOn(0)),
                is(sequence(sequence(1, 3, -4), sequence(7, -9), sequence(2))));
    }

    @Test
    public void supportsSplitOn() throws Exception {
        assertThat(sequence(1, 3, -4, 0, 7, -9, 0, 2).splitOn(0),
                is(pair(sequence(1, 3, -4), sequence(7, -9, 0, 2))));
    }

    @Test
    public void supportsRecursiveSplitWhen() throws Exception {
        assertThat(numbers(1, 3, -4, 5, 7, -9, 0, 2).recursive(Sequences.<Number>splitWhen(Numbers.lessThan(0))),
                is(sequence(numbers(1, 3), numbers(5, 7), numbers(0, 2))));
    }

    @Test
    public void supportsSplitWhen() throws Exception {
        assertThat(numbers(1, 3, -4, 5, 7, -9, 0, 2).splitWhen(Numbers.lessThan(0)),
                is(pair(numbers(1, 3), numbers(5, 7, -9, 0, 2))));
    }

    @Test
    public void supportsReduceRight() throws Exception {
        assertThat(numbers(1, 2, 3).reduceRight(add()), NumberMatcher.is(6));
    }

    @Test
    public void supportsFoldRight() throws Exception {
        assertThat(sequence(1, 2, 3).foldRight(0, add()), NumberMatcher.is(6));
    }

    @Test
    public void supportsFoldRightWithInfiniteSequenceIfFunctionTerminatesEarlyAndUsesPairs() throws Exception {
        assertThat(repeat(false).foldRight(false, andPair()), is(false));
    }

    @Test
    public void supportsReduceRightWithInfiniteSequenceIfFunctionTerminatesEarlyAndUsesPairs() throws Exception {
        assertThat(repeat(true).reduceRight(orPair()), is(true));
    }

    @Test
    public void supportsBreak() throws Exception {
        assertThat(sequence(1, 2, 3, 4, 1, 2, 3, 4).breakOn(greaterThan(3)),
                is(pair(sequence(1, 2, 3), sequence(4, 1, 2, 3, 4))));
        assertThat(sequence(1, 2, 3).breakOn(lessThan(9)),
                is(pair(empty(Integer.class), sequence(1, 2, 3))));
        assertThat(sequence(1, 2, 3).breakOn(greaterThan(9)),
                is(pair(sequence(1, 2, 3), empty(Integer.class))));
    }

    @Test
    public void supportsSpan() throws Exception {
        assertThat(sequence(1, 2, 3, 4, 1, 2, 3, 4).span(lessThan(3)), is(pair(sequence(1, 2), sequence(3, 4, 1, 2, 3, 4))));
        assertThat(sequence(1, 2, 3).span(lessThan(9)), is(pair(sequence(1, 2, 3), Sequences.<Integer>empty())));
        assertThat(sequence(1, 2, 3).span(lessThan(0)), is(pair(Sequences.<Integer>empty(), sequence(1, 2, 3))));
    }

    @Test
    public void supportsSplitAt() throws Exception {
        Sequence<String> data = sequence("Cat", "Dog", "Mouse", "Rabbit", "Monkey");
        assertThat(data.splitAt(2), is(pair(sequence("Cat", "Dog"), sequence("Mouse", "Rabbit", "Monkey"))));
        assertThat(characters("Hello World!").splitAt(6).first().toString(""), is("Hello "));
        assertThat(characters("Hello World!").splitAt(6).second().toString(""), is("World!"));
    }

    @Test
    public void supportsRecursiveSplitAt() throws Exception {
        Sequence<String> data = sequence("Cat", "Dog", "Mouse", "Rabbit", "Monkey");
        assertThat(data.recursive(Sequences.<String>splitAt(2)), is(sequence(sequence("Cat", "Dog"), sequence("Mouse", "Rabbit"), sequence("Monkey"))));
    }

    @Test
    public void supportsEquality() throws Exception {
        assertThat(sequence(1, 2, 3).equals(sequence(1, 2, 3)), is(true));
        assertThat(sequence(1, 2, 3).equals(sequence(3, 2, 1)), is(false));
        assertThat(sequence(1, 2, 3).equals(sequence("1", "2", "3")), is(false));
        assertThat(sequence(1, 2).equals(sequence(1, 2, 3)), is(false));
        assertThat(sequence(1, 2, 3).equals(sequence(1, 2)), is(false));
        assertThat(sequence(1, 2, 3).equals(list(1, 2, 3)), is(true));
        assertThat(sequence(Dates.date(2000, 1, 1), 1).equals(sequence(Dates.date(2000, 1, 1), 1)), is(true));
        assertThat(sequence(Dates.date(2000, 1, 1), 1).equals(sequence(null, 1)), is(false));
        assertThat(sequence(null, 1).equals(sequence(Dates.date(2000, 1, 1), 1)), is(false));
        assertThat(sequence(1, null, 3).equals(sequence(1, 2, 3)), is(false));
        assertThat(sequence(1, 2, 3).equals(sequence(1, null, 3)), is(false));
        assertThat(sequence(1, null, 3).equals(sequence(1, null, 3)), is(true));
    }

    @Test
    public void supportsHashCodes() throws Exception {
        assertThat(sequence(1, 2, 3).hashCode() == sequence(1, 2, 3).hashCode(), is(true));
        assertThat(sequence(1, 2, 3).hashCode() == sequence(3, 2, 1).hashCode(), is(true)); // could we make this false easily?
        assertThat(sequence(1, 2, 3).hashCode() == sequence("1", "2", "3").hashCode(), is(false));
        assertThat(sequence(1, 2).hashCode() == sequence(1, 2, 3).hashCode(), is(false));
        assertThat(sequence(1, 2, 3).hashCode() == sequence(1, 2).hashCode(), is(false));
        assertThat(sequence(1, 2, 3).hashCode() == list(1, 2, 3).hashCode(), is(false));
        assertThat(sequence(1, null, 3, objectWithHashCodeOf(0)).hashCode() != 0, is(true));
    }

    private Object objectWithHashCodeOf(final int value) {
        return new Object() {
            @Override
            public int hashCode() {
                return value;
            }
        };
    }

    @Test
    public void supportsGroupByAndPreservesOrder() throws Exception {
        Sequence<Group<Number, Integer>> groups = sequence(1, 2, 3, 4).groupBy(Numbers.mod(2));
        assertThat(groups.first().key(), NumberMatcher.is(1));
        assertThat(groups.first(), hasExactly(1, 3));
        assertThat(groups.second().key(), NumberMatcher.is(0));
        assertThat(groups.second(), hasExactly(2, 4));
    }

    @Test
    public void supportsToMapAndPreservesOrder() throws Exception {
        Map<Number, List<Integer>> groups = sequence(1, 4, 2, 3).toMap(Numbers.mod(2));
        assertThat(groups.get(0), hasExactly(4, 2));
        assertThat(groups.get(1), hasExactly(1, 3));
    }

    @Test
    public void supportsPartition() throws Exception {
        Pair<Sequence<Integer>, Sequence<Integer>> result = sequence(1, 2, 3, 4).partition(even());
        assertThat(result.first(), hasExactly(2, 4));
        assertThat(result.second(), hasExactly(1, 3));
    }

    @Test
    public void supportsPartitionOnForwardOnlySequence() throws Exception {
        Pair<Sequence<Integer>, Sequence<Integer>> result = sequence(1, 2, 3, 4).forwardOnly().partition(even());
        assertThat(result.first(), hasExactly(2, 4));
        assertThat(result.second(), hasExactly(1, 3));
    }

    @Test
    public void supportsReverse() throws Exception {
        assertThat(sequence(1, 2, 3).reverse(), hasExactly(3, 2, 1));
    }

    @Test
    public void supportsSize() throws Exception {
        assertThat(range(10000000000L, 10000000099L).size(), NumberMatcher.is(100));
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
    public void supportsUnsafeCast() throws Exception {
        Sequence<? extends Predicate<?>> wild = sequence(wildCard());
        Sequence<Predicate<Object>> boring = wild.unsafeCast();
        assertThat(boring.head().matches(new Cat()), is(true));
        assertThat(boring.head().matches(null), is(false));
    }

    private Predicate<?> wildCard() {
        return Predicates.notNullValue();
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

    public static interface Animal {
    }

    public static class Cat implements Animal {
    }

    public static class Dog implements Animal {
    }

    @Test
    public void supportsUniqueAndPreservesOrder() throws Exception {
        assertThat(sequence(1, 2, 1, 4, 3, 2).unique(), hasExactly(1, 2, 4, 3));
        assertThat(sequence("Matt", "Dan", "Matt", "Bob").unique(), hasExactly("Matt", "Dan", "Bob"));
    }

    @Test
    public void supportsUniqueAndCanBeIteratedMultipleTimes() throws Exception {
        Sequence<String> unique = sequence("Matt", "Dan", "Matt", "Bob").unique();
        assertThat(unique, hasExactly("Matt", "Dan", "Bob"));
        assertThat(unique, hasExactly("Matt", "Dan", "Bob"));

        Sequence<String> uniqueWithCallable = sequence("Matt", "Dan", "Dominic", "Mary").unique(Strings.characterAt(0));
        assertThat(uniqueWithCallable, hasExactly("Matt", "Dan"));
        assertThat(uniqueWithCallable, hasExactly("Matt", "Dan"));
    }

    @Test
    public void supportsUniqueWithCallable() throws Exception {
        assertThat(sequence("Matt", "Dan", "Dominic", "Mary").unique(Strings.characterAt(0)), hasExactly("Matt", "Dan"));
    }

    @Test
    public void supportsSort() throws Exception {
        assertThat(sort(sequence(5, 6, 1, 3, 4, 2)), hasExactly(1, 2, 3, 4, 5, 6));
        assertThat(sort(sequence("Matt", "Dan", "Bob")), hasExactly("Bob", "Dan", "Matt"));
    }

    @Test
    public void supportsSortDescending() throws Exception {
        assertThat(sort(sequence(5, 6, 1, 3, 4, 2), Comparators.<Integer>descending()), hasExactly(6, 5, 4, 3, 2, 1));
        assertThat(sequence(5, 6, 1, 3, 4, 2).sortBy(Comparators.<Integer>descending()), hasExactly(6, 5, 4, 3, 2, 1));
        assertThat(sort(sequence("Bob", "Dan", "Matt"), Comparators.<String>descending()), hasExactly("Matt", "Dan", "Bob"));
        assertThat(sequence("Bob", "Dan", "Matt").sortBy(Comparators.<String>descending()), hasExactly("Matt", "Dan", "Bob"));
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
    @Notes("This behaviour is like SQL order by, not 100% convinced this is correct so please give feedback")
    public void whenSortingWithNullsTheyAlwaysComeLast() throws Exception {
        Sequence<Integer> unsorted = sequence(2, null, 1);
        assertThat(unsorted.sortBy(returnArgument(Integer.class)), hasExactly(1, 2, null));
        assertThat(unsorted.sortBy(ascending(returnArgument(Integer.class))), hasExactly(1, 2, null));
        assertThat(unsorted.sortBy(descending(returnArgument(Integer.class))), hasExactly(2, 1, null));
    }

    @Test
    public void supportsSortByWithCompositeComparator() throws Exception {
        Sequence<String> unsorted = Sequences.sequence("dan", "tom", "mateusz", "stuart");
        final Sequence<String> sorted = unsorted.sortBy(comparators(descending(length()), ascending(Callables.<String>returnArgument())));

        assertThat(sorted, hasExactly("mateusz", "stuart", "dan", "tom"));
    }

    @Test
    public void supportsSortBySizeAndLength() throws Exception {
        int[] small = {1};
        String medium = "123";
        List<Integer> large = list(1, 2, 3, 4, 5, 6);
        Sequence<Integer> veryLarge = sequence(1, 2, 3, 4, 5, 6, 7, 8, 9);
        Sequence<Object> unsorted = sequence(large, small, veryLarge, medium);
        assertThat(unsorted.sortBy(size()), hasExactly(small, medium, large, veryLarge));
        assertThat(unsorted.sortBy(ascending(size())), hasExactly(small, medium, large, veryLarge));
        assertThat(unsorted.sortBy(descending(length())), hasExactly(veryLarge, large, medium, small));
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
            if (Numbers.equalTo(number, 1)) {
                return none(); // the conversion didn't work
            }
            if (Numbers.equalTo(number, 2)) {
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
    public void supportsFindingTheFirstSome() throws Exception {
        assertThat(sequence(none(Integer.class), some(2), some(3)).flatMap(identity(Integer.class)).headOption(), is((Option<Integer>) some(2)));
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
    public void canConvertToDeque() throws Exception {
        final Deque<Integer> aList = sequence(1, 2).toDeque();
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
    }

    @Test
    public void canReduceEmptySequence() {
        assertThat(numbers().reduce(sum()), NumberMatcher.is(0));
    }

    @Test
    public void supportsReduceLeft() throws Exception {
        assertThat(numbers(1, 2, 3).reduce(sum()), NumberMatcher.is(6));
        assertThat(numbers(1, 2, 3).reduceLeft(sum()), NumberMatcher.is(6));
    }

    @Test
    public void supportsFoldToACount() throws Exception {
        assertThat(sequence("Dan", "Matt", "Bob").fold(0, count()), NumberMatcher.is(3));
        assertThat(sequence("Dan", "Matt").fold(0, count()), NumberMatcher.is(2));
        assertThat(sequence("Dan").fold(0, count()), NumberMatcher.is(1));
        assertThat(empty().fold(0, count()), NumberMatcher.is(0));
    }

    @Test
    public void supportsFoldLeft() throws Exception {
        assertThat(sequence(1, 2, 3).fold(0, sum()), NumberMatcher.is(6));
        assertThat(sequence(1, 2, 3).foldLeft(0, sum()), NumberMatcher.is(6));
    }

    @Test
    public void supportsForEach() throws Exception {
        final int[] sum = {0};
        sequence(1, 2).each(new Block<Integer>() {
            public void execute(Integer value) {
                sum[0] += value;
            }
        });
        assertThat(sum[0], is(3));
    }

    @Test
    public void supportsEachConcurrently() throws Exception {
        final int[] sum = {0};
        sequence(1, 2).eachConcurrently(new Block<Integer>() {
            public void execute(Integer value) throws InterruptedException {
                sum[0] += value;
            }
        });
        assertThat(sum[0], is(3));
    }

    @Test
    public void supportsMap() throws Exception {
        Sequence<String> strings = sequence(1, 2).map(toString);
        assertThat(strings, hasExactly("1", "2"));
    }

    @Test
    public void supportsConcurrentMap() throws Exception {
        Iterable<String> strings = sequence(1, 2).mapConcurrently(toString);
        assertThat(strings, hasExactly("1", "2"));
    }

    @Test
    public void supportsConcurrentMapWithCustomExecutor() throws Exception {
        ExecutorService executorService = Executors.newCachedThreadPool();
        Iterable<String> strings = sequence(1, 2).mapConcurrently(toString, executorService);
        assertThat(strings, hasExactly("1", "2"));
        executorService.shutdown();
    }

    @Test
    public void mapIsLazy() throws Exception {
        Iterable<Integer> result = sequence(returns(1), callThrows(new Exception(), Integer.class)).
                map(call(Integer.class));
        assertThat(result, startsWith(1));
    }

    @Test
    public void supportsFilter() throws Exception {
        Sequence<Integer> result = sequence(1, 2, 3, 4).filter(even());
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
        Sequence<Character> characters = sequence("Hello").flatMap(toCharacters());
        assertThat(characters, hasExactly('H', 'e', 'l', 'l', 'o'));
    }

    @Test
    public void supportsConcurrentFlatMap() throws Exception {
        Sequence<Character> characters = sequence("Hello").flatMapConcurrently(toCharacters());
        assertThat(characters, hasExactly('H', 'e', 'l', 'l', 'o'));
    }

    @Test
    public void supportsConcurrentFlatMapWithCustomExecutor() throws Exception {
        ExecutorService executorService = Executors.newCachedThreadPool();
        Sequence<Character> characters = sequence("Hello").flatMapConcurrently(toCharacters(), executorService);
        assertThat(characters, hasExactly('H', 'e', 'l', 'l', 'o'));
        executorService.shutdown();
    }

    @Test
    public void supportsFlatten() throws Exception {
        Sequence<Character> characters = Sequences.flatten(sequence("Hello").map(toCharacters()));
        assertThat(characters, hasExactly('H', 'e', 'l', 'l', 'o'));
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
    public void supportsZipToTriple() {
        assertThat(zip(sequence(1, 3, 5), sequence(2, 4, 6, 8), sequence("car", "cat")), hasExactly(triple(1, 2, "car"), triple(3, 4, "cat")));
        assertThat(sequence(1, 3, 5).zip(sequence(2, 4, 6, 8), sequence("car", "cat")), hasExactly(triple(1, 2, "car"), triple(3, 4, "cat")));
    }

    @Test
    public void supportsZipToQuadruple() {
        assertThat(zip(sequence(1, 3, 5), sequence(2, 4, 6, 8), sequence("car", "cat"), sequence('C')), hasExactly(quadruple(1, 2, "car", 'C')));
        assertThat(sequence(1, 3, 5).zip(sequence(2, 4, 6, 8), sequence("car", "cat"), sequence('C')), hasExactly(quadruple(1, 2, "car", 'C')));
    }

    @Test
    public void supportsZipToQuintuple() {
        assertThat(zip(sequence(1, 3, 5), sequence(2, 4, 6, 8), sequence("car", "cat"), sequence('C'), sequence('D')), hasExactly(quintuple(1, 2, "car", 'C', 'D')));
        assertThat(sequence(1, 3, 5).zip(sequence(2, 4, 6, 8), sequence("car", "cat"), sequence('C'), sequence('D')), hasExactly(quintuple(1, 2, "car", 'C', 'D')));
    }

    @Test
    public void supportsZipWithIndex() {
        assertThat(sequence("Dan", "Matt", "Bob").zipWithIndex(), hasExactly(pair((Number) 0, "Dan"), pair((Number) 1, "Matt"), pair((Number) 2, "Bob")));
    }

    @Test
    public void supportsForwardOnly() throws Exception {
        Sequence<Integer> sequence = sequence(1, 2, 3, 4).forwardOnly();

        assertThat(sequence.headOption(), is(option(1)));
        assertThat(sequence.headOption(), is(option(2)));
    }

    @Test
    public void supportsInterruption() throws Exception {
        final int[] count = new int[]{0};
        Sequence<Integer> interruptable = repeat(new Function<Integer>() {
            @Override
            public Integer call() throws Exception {
                if (++count[0] == 5) {
                    currentThread().interrupt();
                }
                return count[0];
            }
        }).interruptable();

        try {
            interruptable.realise();
        } catch (LazyException e) {
            assertThat(e.getCause(), instanceOf(InterruptedException.class));
            assertThat(count[0], is(5));
        }
    }

    @Test
    public void supportsSortingByOtherIterableOrder() throws Exception {
        assertThat(sequence('D', 'E', 'F').sortBy(indexIn(list('F', 'E', 'D'))), hasExactly('F', 'E', 'D'));
        assertThat(sequence(pair("Dan", 'D'), pair("Ray", 'R'), pair("Tom", 'T')).sortBy(second(Character.class).then(indexIn(list('T', 'R', 'D')))),
                hasExactly(pair("Tom", 'T'), pair("Ray", 'R'), pair("Dan", 'D')));
    }

    @Test
    public void supportsWindowed() throws Exception {
        assertThat(sequence(1, 2, 3, 4, 5).windowed(3), is(sequence(sequence(1, 2, 3), sequence(2, 3, 4), sequence(3, 4, 5))));
    }

    @Test
    public void supportsIntersperse() {
        assertThat(sequence("a", "b", "c").intersperse("x"), hasExactly("a", "x", "b", "x", "c"));
        assertThat(sequence("a").intersperse("x"), hasExactly("a"));
        assertThat(sequence().intersperse("x"), isEmpty());
        assertThat(repeat(1).intersperse(0).take(5), hasExactly(1, 0, 1, 0, 1));
        assertThat(repeat(1).intersperse(0).take(6), hasExactly(1, 0, 1, 0, 1, 0));
    }

    @Test
    public void supportsFlatteningToOption() {
        assertThat(sequence("roger", "ramjet").flatOption(), is(some(sequence("roger", "ramjet"))));
        assertThat(flatOption(sequence("roger", "ramjet")), is(some(sequence("roger", "ramjet"))));

        assertThat(empty(String.class).flatOption(), is(Option.<Sequence<String>>none()));
        assertThat(flatOption(empty(String.class)), is(Option.<Sequence<String>>none()));
    }

    @Test
    public void logicalAndOfEmptyListIsTrue() throws Exception {
        assertThat(empty(Boolean.class).reduce(and), is(true));
    }

    @Test
    public void logicalOrOfEmptyListIsFalse() throws Exception {
        assertThat(empty(Boolean.class).reduce(or), is(false));
    }

    @Test
    public void logicalXorOfEmptyListIsFalse() throws Exception {
        assertThat(empty(Boolean.class).reduce(xor), is(false));
    }

    @Test
    public void supportsIndexAccess() {
        assertThat(sequence("a", "b", "c").index(1), is("b"));
        assertThat(sequence("a", "b", "c").indexOf("c"), is(2));
    }
}