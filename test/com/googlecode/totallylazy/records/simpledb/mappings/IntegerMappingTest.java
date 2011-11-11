package com.googlecode.totallylazy.records.simpledb.mappings;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class IntegerMappingTest {
    @Test
    public void supportsToString() throws Exception {
        assertThat(new IntegerMapping().toString(Integer.MIN_VALUE), is("0000000000"));
        assertThat(new IntegerMapping().toString(Integer.MAX_VALUE), is("4294967295"));
    }

    @Test
    public void supportsToValue() throws Exception {
        assertThat(new IntegerMapping().toValue("0000000000"), is(Integer.MIN_VALUE));
        assertThat(new IntegerMapping().toValue("4294967295"), is(Integer.MAX_VALUE));
    }
}
