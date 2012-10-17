package com.googlecode.totallylazy;

import org.junit.Test;

import static com.googlecode.totallylazy.Closeables.close;
import static com.googlecode.totallylazy.Closeables.using;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class CloseablesTest {
    @Test
    public void usingAndCloseShouldBeNullSafe() throws Exception {
        using(null, Runnables.doNothing());
        close(null);
    }

    @Test
    public void shouldSupportObjectsThatHaveCloseMethodButDoNotImplementClosable() throws Exception {
        final int[] count = new int[]{0};
        using(closable(count), Runnables.doNothing());
        assertThat(count[0], is(1));
    }

    private Object closable(final int[] count) {
        return new Object() {
            public void close() {
                count[0]++;
            }
        };
    }
}
