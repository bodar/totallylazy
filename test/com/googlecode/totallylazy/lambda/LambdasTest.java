package com.googlecode.totallylazy.lambda;

import org.junit.Test;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Strings.asBoolean;
import static com.googlecode.totallylazy.lambda.Lambdas.λ;
import static com.googlecode.totallylazy.matchers.IterableMatcher.hasExactly;
import static lambda.Parameters.s;
import static lambda.Parameters.t;
import static org.hamcrest.MatcherAssert.assertThat;

public class LambdasTest {
    @Test
    public void canMap() throws Exception {
        assertThat(sequence("car", "bob").
                map(λ(s, s.toUpperCase())).
                map(λ(t, t.charAt(0))),
                hasExactly('C', 'B'));
    }

    @Test
    public void canParseBoolean() throws Exception {
        assertThat(sequence("true", "false").map(asBoolean()), hasExactly(true, false));
    }
}
