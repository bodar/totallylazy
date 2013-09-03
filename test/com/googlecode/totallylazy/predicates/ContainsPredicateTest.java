package com.googlecode.totallylazy.predicates;

import com.googlecode.totallylazy.Strings;
import com.googlecode.totallylazy.matchers.Matchers;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;

public class ContainsPredicateTest {

    @Test
    public void supportsToString() throws Exception {
        assertThat(Strings.contains("13").toString(), Matchers.is("contains '13'"));
    }
}
