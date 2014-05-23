package com.googlecode.totallylazy;


public interface PredicateAssert {
    public static <T> void assertThat(T actual, Predicate<? super T> predicate) {
        assertThat("", actual, predicate);
    }

    public static <T> void assertThat(String reason, T actual, Predicate<? super T> predicate) {
        if (!predicate.matches(actual)) {
            throw new AssertionError(reason + "\nExpected: " + predicate + "\n  Actual: " + actual);
        }
    }

    public static void assertThat(String reason, boolean assertion) {
        if (!assertion) {
            throw new AssertionError(reason);
        }
    }
}
