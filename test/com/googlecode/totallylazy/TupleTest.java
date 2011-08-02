package com.googlecode.totallylazy;

import org.junit.Test;

import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.Triple.triple;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class TupleTest {
    @Test
    public void equality() throws Exception {
        assertThat(pair(1, "Blah").equals(pair(1, "Blah")), is(true));
        assertThat(pair(1, "Blah").equals(pair(2, "Blah")), is(false));
        assertThat(triple(1, "Blah", 3L).equals(triple(1, "Blah", 3L)), is(true));
        assertThat(triple(1, "Blah", 3L).equals(triple(1, "Blah", 4L)), is(false));
    }

    @Test
    public void hashable() throws Exception {
        assertThat(pair(1, "Blah").hashCode() == pair(1, "Blah").hashCode(), is(true));
        assertThat(pair(1, "Blah").hashCode() == pair(2, "Blah").hashCode(), is(false));
        assertThat(triple(1, "Blah", 3L).hashCode() == triple(1, "Blah", 3L).hashCode(), is(true));
        assertThat(triple(1, "Blah", 3L).hashCode() == triple(1, "Blah", 4L).hashCode(), is(false));
    }
}
