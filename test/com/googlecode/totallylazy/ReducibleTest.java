package com.googlecode.totallylazy;

import com.googlecode.totallylazy.numbers.Numbers;
import org.junit.Test;

import static com.googlecode.totallylazy.Sequences.repeat;
import static com.googlecode.totallylazy.Sequences.sequence;

public class ReducibleTest {
    static boolean even(int a) { return a % 2 == 0 ; }
    static int inc(int a) { return a + 1; }
    static int add(int a, int b) { return a + b; }

    @Test
    public void supportsReduction() throws Exception {
        Reducible<Integer, Number> map = sequence(1, 2, 3, 4).
                <Number>reducible().
                flatMap(i -> repeat(i).take(3).reducible()).
                filter(ReducibleTest::even);
        Number result = map.reduce(Numbers.lcm);
        System.out.println("result = " + result);

    }
}
