package com.googlecode.totallylazy.structural;

import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Some;
import org.junit.Test;

import java.io.Closeable;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.googlecode.totallylazy.Option.none;
import static com.googlecode.totallylazy.matchers.Matchers.is;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;

public class StructuralTest {
    @Test
    public void canStructurallyCast() throws Exception {
        final AtomicBoolean called = new AtomicBoolean(false);
        Object closeable = new Object() {
            void close() {
                called.set(true);
            }
        };

        Closeable close = Structural.cast(Closeable.class, closeable);
        close.close();
        assertThat(called.get(), is(true));
    }

    @Test
    public void throwsWhenDoesNotFulfillStructuralContract() throws Exception {
        try {
            Structural.cast(Closeable.class, new Object());
        } catch (ClassCastException e) {
        }
    }

    @Test
    public void canCastOption() throws Exception {
        assertThat(Structural.castOption(Closeable.class, new Object() { void close() {} }), instanceOf(Some.class));
        assertThat(Structural.castOption(Closeable.class, new Object()), is(none(Closeable.class)));
    }

    @Test
    public void canCheckInstanceOf() throws Exception {
        assertThat(Structural.instanceOf(Closeable.class, new Object() { void close() {} }), is(true));
        assertThat(Structural.instanceOf(Closeable.class, new Object()), is(false));
    }
}
