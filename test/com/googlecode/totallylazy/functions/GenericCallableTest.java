package com.googlecode.totallylazy.functions;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class GenericCallableTest {
    @Test
    public void canGetGenericType() throws Exception {
        final GenericCallable<String> genericCallable = new GenericCallable<String>() {
            public String call() throws Exception {
                throw new UnsupportedOperationException();
            }
        };

        assertTrue(genericCallable.forClass().equals(String.class));
    }
}
