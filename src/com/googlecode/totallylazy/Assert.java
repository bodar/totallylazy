package com.googlecode.totallylazy;


import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Predicates.sameInstance;

public interface Assert {
    static <T> void assertThat(T actual, Predicate<? super T> predicate) {
        assertThat("", actual, predicate);
    }

    static <T> void assertThat(String reason, T actual, Predicate<? super T> predicate) {
        if (!predicate.matches(actual)) {
            throw new AssertionError(reason + "\nExpected: " + predicate + "\n  Actual: " + actual);
        }
    }

    static void assertThat(String reason, boolean assertion) {
        if (!assertion) throw new AssertionError(reason);
    }

    static public <T> void assertEquals(T expected, T actual) {
        assertThat(actual, is(expected));
    }

    static void assertTrue(boolean assertion) {
        assertTrue("", assertion);
    }

    static void assertTrue(String reason, boolean assertion) {
        assertThat(reason, assertion);
    }

    static void assertFalse(boolean assertion) {
        assertFalse("", assertion);
    }

    static void assertFalse(String reason, boolean assertion) {
        assertThat(reason, !assertion);
    }

    static public <T> void assertSame(T expected, T actual) {
        assertThat(actual, sameInstance(expected));
    }

    static void fail() {
        fail("");
    }

    static void fail(String message) {
        throw new AssertionError(message);
    }
}
