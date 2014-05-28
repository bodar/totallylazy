package com.googlecode.totallylazy.predicates;

import com.googlecode.totallylazy.Strings;
import org.junit.Test;

import static com.googlecode.totallylazy.PredicateAssert.assertThat;
import static com.googlecode.totallylazy.Predicates.is;

public class EndsWithPredicateTest {
    @Test
    public void supportsToString() throws Exception {
        assertThat(Strings.endsWith("13").toString(), is("ends with '13'"));
    }

    @Test
    public void supportsEqualityOnPredicateItself() throws Exception {
        assertThat(Strings.endsWith("13").equals(Strings.endsWith("13")), is(true));
        assertThat(Strings.endsWith("13").equals(Strings.endsWith("14")), is(false));
    }
}
