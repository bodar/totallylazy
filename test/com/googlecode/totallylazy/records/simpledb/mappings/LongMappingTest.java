package com.googlecode.totallylazy.records.simpledb.mappings;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class LongMappingTest {
    @Test
    public void supportsToString() throws Exception {
        assertThat(new LongMapping().toString(Long.MIN_VALUE), is("00000000000000000000"));
        assertThat(new LongMapping().toString(Long.MAX_VALUE), is("18446744073709551615"));
    }

    @Test
    public void supportsToValue() throws Exception {
        assertThat(new LongMapping().toValue("00000000000000000000"), is(Long.MIN_VALUE));
        assertThat(new LongMapping().toValue("18446744073709551615"), is(Long.MAX_VALUE));
    }
}
