package com.googlecode.totallylazy.predicates;

import org.junit.Test;

import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.predicates.GreaterThanBinaryPredicate.greaterThan;
import static com.googlecode.totallylazy.PredicateAssert.assertThat;
import static com.googlecode.totallylazy.Predicates.instanceOf;

public class GreaterThanBinaryPredicateTest {
    public static final GreaterThanBinaryPredicate<Integer> greaterThan = greaterThan(Integer.class);

    @Test
    public void canMatch() throws Exception {
        assertThat(greaterThan.matches(2, 3), is(false));
        assertThat(greaterThan.matches(3, 3), is(false));
        assertThat(greaterThan.matches(4, 3), is(true));
    }

    @Test
    public void canCallWithTwoArguments() throws Exception {
        assertThat(greaterThan.call(2, 3), is(false));
        assertThat(greaterThan.call(3, 3), is(false));
        assertThat(greaterThan.call(4, 3), is(true));
    }

    @Test
    public void canApply() throws Exception {
        assertThat(greaterThan.apply(2).apply(3), is(false));
        assertThat(greaterThan.apply(3).apply(3), is(false));
        assertThat(greaterThan.apply(4).apply(3), is(true));
    }

    @Test
    public void canCallWithOneArgument() throws Exception {
        assertThat(greaterThan.call(2).call(3), is(false));
        assertThat(greaterThan.call(3).call(3), is(false));
        assertThat(greaterThan.call(4).call(3), is(true));
    }

    @Test
    public void canFlip() throws Exception {
        assertThat(greaterThan.flip().matches(3, 2), is(false));
        assertThat(greaterThan.flip().matches(3, 3), is(false));
        assertThat(greaterThan.flip().matches(3, 4), is(true));
    }

    @Test
    public void canApplySecond() throws Exception {
        assertThat(greaterThan.applySecond(3).apply(2), is(false));
        assertThat(greaterThan.applySecond(3).apply(3), is(false));
        assertThat(greaterThan.applySecond(3).apply(4), is(true));
    }

    @Test
    public void allMethodsReturnOtherWellKnownPredicates() throws Exception {
        assertThat(greaterThan.applySecond(4), instanceOf(GreaterThanPredicate.class));
        assertThat(greaterThan.apply(4), instanceOf(LessThanPredicate.class));
        assertThat(greaterThan.call(4), instanceOf(LessThanPredicate.class));
        assertThat(greaterThan.flip(), instanceOf(LessThanBinaryPredicate.class));
    }
}
