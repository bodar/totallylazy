package com.googlecode.totallylazy;

import org.junit.Test;

import static com.googlecode.totallylazy.IList.list;
import static com.googlecode.totallylazy.matchers.IterableMatcher.hasExactly;
import static org.hamcrest.MatcherAssert.assertThat;

public class IListTest {
    @Test
    public void works() throws Exception {
        final IList<Integer> cons = list(1).cons(2);
        assertThat(cons, hasExactly(2, 1));
    }
}
