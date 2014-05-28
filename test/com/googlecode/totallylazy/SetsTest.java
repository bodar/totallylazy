package com.googlecode.totallylazy;

import org.junit.Test;

import java.util.Set;

import static com.googlecode.totallylazy.Predicates.contains;
import static com.googlecode.totallylazy.Sets.intersection;
import static com.googlecode.totallylazy.Sets.set;
import static com.googlecode.totallylazy.Sets.union;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Assert.assertThat;

public class SetsTest {
    @Test
    public void supportsUnion() throws Exception {
        Set<Integer> union = union(set(1, 2, 3), set(2, 3, 4));
        assertThat(union.size(), is(4));
        assertThat(union, contains(1, 2, 3, 4));
    }

    @Test
    public void supportsIntersection() throws Exception {
        Set<Integer> intersection = intersection(set(1, 2, 3), set(2, 3, 4));
        assertThat(intersection.size(), is(2));
        assertThat(intersection, contains(2, 3));
        assertThat(intersection(set(1, 2, 3), set(2, 3, 4), set(3)), contains(3));
    }

    @Test
    public void supportsComplement() throws Exception {
        Set<Integer> intersection = Sets.complement(set(1, 2, 3), set(2, 3, 4));
        assertThat(intersection.size(), is(1));
        assertThat(intersection, contains(1));
    }
}
