package com.googlecode.totallylazy.predicates;

import com.googlecode.totallylazy.Strings;
import com.googlecode.totallylazy.matchers.Matchers;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;

public class StartsWithPredicateTest {
    @Test
    public void supportsToString() throws Exception {
        assertThat(Strings.startsWith("13").toString(), Matchers.is("starts with '13'"));
    }

    @Test
    public void supportsEqualityOnPredicateItself() throws Exception {
        assertThat(Strings.startsWith("13").equals(Strings.startsWith("13")), Matchers.is(true));
        assertThat(Strings.startsWith("13").equals(Strings.startsWith("14")), Matchers.is(false));
    }
}
