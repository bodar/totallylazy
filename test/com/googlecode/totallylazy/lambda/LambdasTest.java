package com.googlecode.totallylazy.lambda;

import lambda.annotation.LambdaParameter;
import org.junit.Test;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.lambda.Lambdas.λ;
import static com.googlecode.totallylazy.matchers.IterableMatcher.hasExactly;
import static org.hamcrest.MatcherAssert.assertThat;

public class LambdasTest {
    @LambdaParameter
    public static String s;

    @Test
    public void canMap() throws Exception {
        assertThat(sequence("car", "bob").map(λ(s, s.toUpperCase())), hasExactly("CAR", "BOB"));
    }
}
