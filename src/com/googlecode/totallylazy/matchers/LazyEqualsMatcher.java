package com.googlecode.totallylazy.matchers;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.Callers.call;

public class LazyEqualsMatcher<T> extends TypeSafeMatcher<T> {
    private final Callable<T> expectedLoader;
    private final String descriptionText;

    private LazyEqualsMatcher(String descriptionText, Callable<T> expectedLoader) {
        this.expectedLoader = expectedLoader;
        this.descriptionText = descriptionText;
    }

    @Override
    protected boolean matchesSafely(T actual) {
        return call(expectedLoader).equals(actual);
    }

    public void describeTo(Description description) {
        description.appendText(descriptionText);
    }

    public static <E> LazyEqualsMatcher<E> lazyEqualTo(String descriptionText, Callable<E> expectedLoader) {
        return new LazyEqualsMatcher<E>(descriptionText, expectedLoader);
    }
}
