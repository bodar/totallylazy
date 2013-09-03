package com.googlecode.totallylazy.predicates;

import com.googlecode.totallylazy.Strings;
import com.googlecode.totallylazy.matchers.Matchers;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;

public class EndsWithPredicateTest {
    @Test
    public void supportsToString() throws Exception {
        assertThat(Strings.endsWith("13").toString(), Matchers.is("ends with '13'"));
    }

    @Test
    public void supportsEqualityOnPredicateItself() throws Exception {
        assertThat(Strings.endsWith("13").equals(Strings.endsWith("13")), Matchers.is(true));
        assertThat(Strings.endsWith("13").equals(Strings.endsWith("14")), Matchers.is(false));
    }
}
