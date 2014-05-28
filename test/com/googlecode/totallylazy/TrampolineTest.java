package com.googlecode.totallylazy;

import org.junit.Test;

import static com.googlecode.totallylazy.Trampoline.done;
import static com.googlecode.totallylazy.Trampoline.more;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Assert.assertThat;

public class TrampolineTest {
    Trampoline<Boolean> even(int n) {
        if (n == 0) return done(true);
        return more(() -> odd(n - 1));
    }

    Trampoline<Boolean> odd(int n) {
        if (n == 0) return done(false);
        return more(() -> even(n - 1));
    }

    @Test
    public void doesNotBlowStack() throws Exception {
        assertThat(even(99999).apply(), is(false));
    }
}