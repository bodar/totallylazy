package com.googlecode.totallylazy;

import org.junit.Test;

import java.lang.*;
import java.lang.Iterable;

import static com.googlecode.totallylazy.Callables.*;
import static com.googlecode.totallylazy.Iterables.list;
import static com.googlecode.totallylazy.Predicates.even;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.hasItem;
import static org.junit.matchers.JUnitMatchers.hasItems;

public class IterableTest {
    @Test
    public void supportsToString() throws Exception {
        assertThat(list(1, 2, 3).toString(), is("1,2,3"));
        assertThat(list(1, 2, 3).toString(":"), is("1:2:3"));
        assertThat(list(1, 2, 3).toString("(", ", ", ")"), is("(1, 2, 3)"));
    }

    @Test
    public void supportsReduceLeft() throws Exception {
        int sum = list(1, 2, 3).reduceLeft(add());
        assertThat(sum, is(6));
    }

    @Test
    public void supportsFoldLeft() throws Exception {
        int sum = list(1, 2, 3).foldLeft(0, add());
        assertThat(sum, is(6));
    }

    @Test
    public void supportsTail() throws Exception {
        assertThat(list(1, 2, 3).tail(), hasItems(2, 3));
    }

    @Test
    public void supportsHead() throws Exception {
        assertThat(list(1, 2).head(), is(1));
    }

    @Test
    public void supportsForEach() throws Exception {
        final int[] sum = {0};
        list(1, 2).foreach(new Runnable1<Integer>() {
            public void run(Integer value) {
                sum[0] += value;
            }
        });
        assertThat(sum[0], is(3));
    }

    @Test
    public void supportsMap() throws Exception {
        Iterable<String> strings = list(1, 2).map(asString(Integer.class));
        assertThat(strings, hasItems("1", "2"));
    }

    @Test
    public void mapIsLazy() throws Exception {
        Iterable<Integer> result = list(returns(1), callThrows(new Exception(), Integer.class)).
                map(invokeCall(Integer.class));
        assertThat(result, hasItem(1));
    }

    @Test
    public void supportsFilter() throws Exception {
        Iterable<Integer> result = list(1, 2, 3, 4).filter(even());
        assertThat(result, hasItems(2, 4));
    }

    @Test
    public void filterIsLazy() throws Exception {
        Iterable<Integer> result = list(returns(1), returns(2), callThrows(new Exception(), Integer.class)).
                map(invokeCall(Integer.class)).
                filter(even());
        assertThat(result, hasItem(2));
    }

    @Test
    public void supportsFlatMap() throws Exception {
        Iterable<Integer> result = list(1, 2, 3).flatMap(new Callable1<Integer, Iterable<Integer>>() {
            public Iterable<Integer> call(Integer value) throws Exception {
                return list(value, value * 3);
            }
        });
        assertThat(result, hasItems(1, 2, 3, 6, 9));
    }
}
