package com.googlecode.totallylazy;

import org.junit.Test;

import java.io.FileNotFoundException;
import java.text.ParseException;

import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class LazyExceptionTest {
    @Test
    public void shouldUnwrapSpecificCheckedCause() throws Exception {
        final ParseException expected = new ParseException("blah blah", 1);
        final LazyException lazyException = new LazyException(expected);

        checkException(lazyException, ParseException.class, expected);
    }

    @Test
    public void shouldUnwrapAlwaysThrowRuntimeCause() throws Exception {
        final RuntimeException runtimeException = new IllegalArgumentException();
        final LazyException lazyException = new LazyException(runtimeException);

        checkException(lazyException, ParseException.class, runtimeException);
    }

    @Test
    public void shouldUnwrapThrowOriginalExceptionWhenNotSpecifiedCheckedExceptionIsTheCause() throws Exception {
        final FileNotFoundException differentChecked = new FileNotFoundException();
        final LazyException lazyException = new LazyException(differentChecked);

        checkException(lazyException, ParseException.class, lazyException);
    }

    private void checkException(LazyException lazyException, Class<? extends Exception> unwrap, Exception expected) {
        try {
            throw lazyException.unwrap(unwrap);
        } catch (Exception e) {
            assertThat(e, sameInstance(expected));
        }
    }
}