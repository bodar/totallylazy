package com.googlecode.totallylazy;

import org.junit.Test;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Strings.toLowerCase;
import static com.googlecode.totallylazy.Strings.toUpperCase;
import static com.googlecode.totallylazy.matchers.IterableMatcher.hasExactly;
import static org.hamcrest.MatcherAssert.assertThat;

public class StringsTest {
    @Test
    public void canMapToStringFunctions() throws Exception {
       assertThat(sequence("Dan").map(toLowerCase()), hasExactly("dan"));
       assertThat(sequence("Dan").map(toUpperCase()), hasExactly("DAN"));
    }


}
