package com.googlecode.totallylazy.matchers;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.Callers.call;

public class LazyEqualsMatcher<T> extends TypeSafeMatcher<T> {
    private final Callable<T> expectedLoader;
    private final String descriptionText;
    private T actual;
    private T expected;

    private LazyEqualsMatcher(String descriptionText, Callable<T> expectedLoader) {
        this.expectedLoader = expectedLoader;
        this.descriptionText = descriptionText;
    }

    @Override
    protected boolean matchesSafely(T actualValue) {
        actual = actualValue;
        expected = call(expectedLoader);
        
        return expected.equals(actual);
    }

    public void describeTo(Description description) {
        description.appendText(createDescriptionText());
    }

    private String createDescriptionText() {
        return expected != null ? String.format(descriptionText + " [expected: %s, actual: %s]", expected, actual) : descriptionText;
    }

    public static <E> LazyEqualsMatcher<E> lazyEqualTo(String descriptionText, Callable<E> expectedLoader) {
        return new LazyEqualsMatcher<E>(descriptionText, expectedLoader);
    }
}
