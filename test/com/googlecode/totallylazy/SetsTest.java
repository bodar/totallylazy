package com.googlecode.totallylazy;

import org.junit.Test;

import java.util.Set;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Sets.intersection;
import static com.googlecode.totallylazy.Sets.set;
import static com.googlecode.totallylazy.Sets.union;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.matchers.JUnitMatchers.hasItems;

public class SetsTest {
    @Test
    public void supportsUnion() throws Exception {
        Set<Integer> union = union(set(1, 2, 3), set(2, 3, 4));
        assertThat(union.size(), is(4));
        assertThat(union, hasItems(1, 2, 3, 4));
        assertThat(sequence(1, 2, 3).union(sequence(2, 3, 4)), hasItems(1, 2, 3, 4));
    }

    @Test
    public void supportsIntersection() throws Exception {
        Set<Integer> intersection = intersection(set(1, 2, 3), set(2, 3, 4));
        assertThat(intersection.size(), is(2));
        assertThat(intersection, hasItems(2, 3));
        assertThat(sequence(1, 2, 3).intersection(sequence(2, 3, 4)), hasItems(2, 3));
        assertThat(intersection(set(1, 2, 3), set(2, 3, 4), set(3)), hasItems(3));
    }

}
