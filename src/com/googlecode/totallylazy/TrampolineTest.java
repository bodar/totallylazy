package com.googlecode.totallylazy.collections;

import org.junit.Test;

import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.collections.Trampoline.done;
import static com.googlecode.totallylazy.collections.Trampoline.more;
import static com.googlecode.totallylazy.collections.Trampoline.trampoline;
import static com.googlecode.totallylazy.matchers.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TrampolineTest {
    public Trampoline<Boolean> even(final int n) {
        if (n == 0) return done(true);
        else return more(new Callable<Trampoline<Boolean>>() {
            @Override
            public Trampoline<Boolean> call() throws Exception {
                return odd(n - 1);
            }
        });
    }

    public Trampoline<Boolean> odd(final int n) {
        if (n == 0) return done(false);
        else return more(new Callable<Trampoline<Boolean>>() {
            @Override
            public Trampoline<Boolean> call() throws Exception {
                return even(n - 1);
            }
        });
    }

    @Test
    public void doesntBlowStack() throws Exception {
        assertThat(even(99999).trampoline(), is(false));
    }


}
