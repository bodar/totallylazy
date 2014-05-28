package com.googlecode.totallylazy.predicates;

import com.googlecode.totallylazy.Strings;
import org.junit.Test;

import static com.googlecode.totallylazy.Assert.assertThat;
import static com.googlecode.totallylazy.Predicates.is;

public class ContainsPredicateTest {

    @Test
    public void supportsToString() throws Exception {
        assertThat(Strings.contains("13").toString(), is("contains '13'"));
    }

    @Test
    public void supportsEqualityOnPredicateItself() throws Exception {
        assertThat(Strings.contains("13").equals(Strings.contains("13")), is(true));
        assertThat(Strings.contains("13").equals(Strings.contains("14")), is(false));
    }
}
