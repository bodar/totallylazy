package com.googlecode.totallylazy.predicates;

import com.googlecode.totallylazy.Strings;
import org.junit.Test;

import static com.googlecode.totallylazy.Assert.assertThat;
import static com.googlecode.totallylazy.Predicates.is;

public class StartsWithPredicateTest {
    @Test
    public void supportsToString() throws Exception {
        assertThat(Strings.startsWith("13").toString(), is("starts with '13'"));
    }

    @Test
    public void supportsEqualityOnPredicateItself() throws Exception {
        assertThat(Strings.startsWith("13").equals(Strings.startsWith("13")), is(true));
        assertThat(Strings.startsWith("13").equals(Strings.startsWith("14")), is(false));
    }
}
