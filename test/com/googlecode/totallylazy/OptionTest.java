package com.googlecode.totallylazy;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import static com.googlecode.totallylazy.Option.some;
import static com.googlecode.totallylazy.Sequences.size;
import static org.junit.Assert.assertThat;

public class OptionTest {
    @Test
    public void areIterable() throws Exception {
        Some<Integer> integer = some(1);
        assertThat(size(integer), CoreMatchers.is(1));
    }
}
