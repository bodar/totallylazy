package com.googlecode.totallylazy.predicates;

import com.googlecode.totallylazy.Sets;
import org.junit.Test;

import java.util.Set;

import static com.googlecode.totallylazy.Predicates.never;
import static com.googlecode.totallylazy.matchers.Matchers.is;
import static com.googlecode.totallylazy.predicates.InPredicate.in;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;

public class InPredicateTest {
    @Test
    public void collapsesEmptyInClauseToFalse() throws Exception {
        Set<Integer> empty = Sets.set();
        LogicalPredicate<Integer> predicate = in(empty);
        assertThat(empty.contains(1), is(predicate.matches(1)));
        assertThat(predicate, instanceOf(never().getClass()));
    }
}
