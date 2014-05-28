package com.googlecode.totallylazy;

import org.junit.Test;

import static com.googlecode.totallylazy.Sequences.repeat;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.numbers.Numbers.lcm;
import static com.googlecode.totallylazy.PredicateAssert.assertThat;

public class ReducibleTest {
    static boolean even(int a) { return a % 2 == 0; }

    static int inc(int a) { return a + 1; }

    static int add(int a, int b) { return a + b; }

    @Test
    public void supportsReduction() throws Exception {
        assertThat(sequence(1, 2, 3, 4).
                reducible().
                flatMap(i -> repeat(i).take(3).reducible()).
                filter(ReducibleTest::even).
                reduce(lcm), is(4));
    }
}
