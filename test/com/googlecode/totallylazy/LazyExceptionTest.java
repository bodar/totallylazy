package com.googlecode.totallylazy;

import org.junit.Test;

import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;

import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;

public class LazyExceptionTest {
    @Test
    public void neverContainsInvocationTargetException() throws Exception {
        final ParseException expected = new ParseException("blah blah", 1);
        
        final InvocationTargetException invocationTargetException = new InvocationTargetException(expected);
        final LazyException lazyException = LazyException.lazyException(LazyException.lazyException(LazyException.lazyException(invocationTargetException)));

        checkException(lazyException, ParseException.class, expected);
    }

    @Test
    public void neverContainsItSelf() throws Exception {
        final ParseException expected = new ParseException("blah blah", 1);
        final LazyException lazyException = LazyException.lazyException(LazyException.lazyException(LazyException.lazyException(expected)));

        checkException(lazyException, ParseException.class, expected);
    }

    @Test
    public void shouldUnwrapSpecificCheckedCause() throws Exception {
        final ParseException expected = new ParseException("blah blah", 1);
        final LazyException lazyException = LazyException.lazyException(expected);

        checkException(lazyException, ParseException.class, expected);
    }

    @Test
    public void shouldUnwrapThrowOriginalExceptionWhenNotSpecifiedCheckedExceptionIsTheCause() throws Exception {
        final FileNotFoundException differentChecked = new FileNotFoundException();
        final LazyException lazyException = LazyException.lazyException(differentChecked);

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