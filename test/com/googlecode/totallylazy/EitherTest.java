package com.googlecode.totallylazy;

import com.googlecode.totallylazy.matchers.NumberMatcher;
import com.googlecode.totallylazy.numbers.Numbers;
import org.junit.Test;

import java.util.NoSuchElementException;

import static com.googlecode.totallylazy.Left.left;
import static com.googlecode.totallylazy.Predicates.isLeft;
import static com.googlecode.totallylazy.Predicates.isRight;
import static com.googlecode.totallylazy.Right.right;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.matchers.IterableMatcher.hasExactly;
import static com.googlecode.totallylazy.numbers.Numbers.DIVIDE_BY_ZERO;
import static com.googlecode.totallylazy.numbers.Numbers.add;
import static com.googlecode.totallylazy.numbers.Numbers.divide;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class EitherTest {
    @Test
    public void canBeUsedInFilterAndMap() throws Exception {
        final Sequence<Either<String, Integer>> eithers = sequence(Left.<String, Integer>left("error"), Right.<String, Integer>right(3));
        assertThat(eithers.filter(isLeft()).map(Callables.left(String.class)), hasExactly("error"));
        assertThat(eithers.filter(isRight()).map(Callables.right(Integer.class)), hasExactly(3));
    }

    @Test
    public void supportsMap() throws Exception {
        assertThat(Either.right(3).map(add(2)), is(Either.right((Number) 5)));
        assertThat(left((Number) 3).map(add(2), null), NumberMatcher.is(5));
        assertThat(right((Number) 3).map(null, add(2)), NumberMatcher.is(5));
    }

    @Test
    public void supportsFlatMap() throws Exception {
        assertThat(Either.<Exception, Number>right(4).flatMap(divide(2).orException()), is(Either.<Exception, Number>right(2)));
        assertThat(Either.<Exception, Number>right(4).flatMap(divide(0).orException()), is(Either.<Exception, Number>left(DIVIDE_BY_ZERO)));
    }

    @Test
    public void supportsFlatten() throws Exception {
        Right<Exception, Either<Exception, Number>> error = right(Either.<Exception, Number>left(DIVIDE_BY_ZERO));
        assertThat(Either.flatten(error), is(Either.<Exception, Number>left(DIVIDE_BY_ZERO)));
        Right<Exception, Either<Exception, Number>> correct = right(Either.<Exception, Number>right(1));
        assertThat(Either.flatten(correct), is(Either.<Exception, Number>right(1)));
    }

    @Test
    public void supportsFold() throws Exception {
        assertThat(left((Number)3).fold(2, add(), null), NumberMatcher.is(5));
        assertThat(right((Number)3).fold(2, null, add()), NumberMatcher.is(5));
    }

    @Test
    public void supportsCreatingRights() throws Exception {
        Either<Exception, Integer> either = right(3);
        assertThat(either.isRight(), is(true));
        assertThat(either.isLeft(), is(false));

        assertThat(either.right(), is(3));
    }

    @Test
    public void supportsCreatingLefts() throws Exception {
        final NumberFormatException exception = new NumberFormatException("waah");
        Either<NumberFormatException, Integer> either = left(exception);
        assertThat(either.isRight(), is(false));
        assertThat(either.isLeft(), is(true));

        assertThat(either.left(), is(exception));
    }

    @Test(expected = NoSuchElementException.class)
    public void doesNotSupportGettingLeftFromARight() throws Exception {
        Either<Exception, Integer> either = right(3);
        either.left();
    }

    @Test(expected = NoSuchElementException.class)
    public void doesNotSupportGettingRightFromALeft() throws Exception {
        Either<RuntimeException, Integer> either = left(new RuntimeException());
        either.right();
    }


}
