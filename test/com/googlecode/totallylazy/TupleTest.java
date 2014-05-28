package com.googlecode.totallylazy;

import org.junit.Test;

import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.Quadruple.quadruple;
import static com.googlecode.totallylazy.Triple.triple;
import static com.googlecode.totallylazy.PredicateAssert.assertThat;
import static com.googlecode.totallylazy.Predicates.is;

public class TupleTest {
    @Test
    public void supportsEquality() throws Exception {
        assertThat(pair(1, "Blah").equals(pair(1, "Blah")), is(true));
        assertThat(pair(1, "Blah").equals(pair(2, "Blah")), is(false));
        assertThat(pair(1, "Blah").equals(triple(2, "Blah", 3L)), is(false));
        assertThat(triple(1, "Blah", 3L).equals(triple(1, "Blah", 3L)), is(true));
        assertThat(triple(1, "Blah", 3L).equals(triple(1, "Blah", 4L)), is(false));
        assertThat(quadruple(1, "Blah", 3L, 'C').equals(quadruple(1, "Blah", 3L, 'C')), is(true));
        assertThat(quadruple(1, "Blah", 3L, 'C').equals(quadruple(1, "Blah", 3L, 'D')), is(false));
        assertThat(quadruple(1, "Blah", 3L, 'C').equals(triple(1, "Blah", 3L)), is(false));
    }

    @Test
    public void supportHashCode() throws Exception {
        assertThat(pair(1, "Blah").hashCode() == pair(1, "Blah").hashCode(), is(true));
        assertThat(pair(1, "Blah").hashCode() == pair(2, "Blah").hashCode(), is(false));
        assertThat(triple(1, "Blah", 3L).hashCode() == triple(1, "Blah", 3L).hashCode(), is(true));
        assertThat(triple(1, "Blah", 3L).hashCode() == triple(1, "Blah", 4L).hashCode(), is(false));
        assertThat(quadruple(1, "Blah", 3L, 'C').hashCode() == quadruple(1, "Blah", 3L, 'C').hashCode(), is(true));
        assertThat(quadruple(1, "Blah", 3L, 'C').hashCode() == quadruple(1, "Blah", 3L, 'D').hashCode(), is(false));
    }

    @Test
    public void canLeftShift() throws Exception {
        assertThat(Pair.leftShift(pair(1, 2), 3), is(pair(2, 3)));
        assertThat(Triple.leftShift(triple(1, 2, 3), 4), is(triple(2, 3, 4)));
    }

}
