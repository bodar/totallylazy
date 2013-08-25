package com.googlecode.totallylazy.structural;

import org.junit.Test;

import java.io.Closeable;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.googlecode.totallylazy.matchers.Matchers.is;
import static com.googlecode.totallylazy.structural.StructuralTypes.structrualType;
import static org.hamcrest.MatcherAssert.assertThat;

public class StructuralTypesTest {
    @Test
    public void canCreateStructuralType() throws Exception {
        final AtomicBoolean called = new AtomicBoolean(false);
        Object closeable = new Object() {
            void close() {
                called.set(true);
            }
        };

        Closeable close = structrualType(Closeable.class, closeable);
        close.close();
        assertThat(called.get(), is(true));
    }

    @Test
    public void throwsWhenDoesNotFulfillStructuralContract() throws Exception {
        try {
            structrualType(Closeable.class, new Object());
        } catch (ClassCastException e) {
        }
    }
}
