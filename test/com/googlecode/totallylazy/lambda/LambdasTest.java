package com.googlecode.totallylazy.lambda;

import org.junit.Ignore;
import org.junit.Test;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.lambda.Lambdas.λ;
import static com.googlecode.totallylazy.matchers.IterableMatcher.hasExactly;
import static org.enumerable.lambda.Parameters.s;
import static org.enumerable.lambda.Parameters.t;
import static org.hamcrest.MatcherAssert.assertThat;

public class LambdasTest {
    @Test
    @Ignore("Must be run with -javaagent:lib/optional/enumerable-java-0.4.0.jar")
    public void canMap() throws Exception {
        assertThat(sequence("car", "bob").
                map(λ(s, s.toUpperCase())).
                map(λ(t, t.charAt(0))),
                hasExactly('C', 'B'));
    }
}
