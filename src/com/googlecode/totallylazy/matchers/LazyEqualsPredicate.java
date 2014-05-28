package com.googlecode.totallylazy.matchers;

import com.googlecode.totallylazy.Callers;
import com.googlecode.totallylazy.predicates.LogicalPredicate;

import java.util.concurrent.Callable;

public class LazyEqualsPredicate<T> extends LogicalPredicate<T> {
    private final Callable<? extends T> expectedLoader;
    private final String descriptionText;
    private T actual;
    private T expected;

    private LazyEqualsPredicate(String descriptionText, Callable<? extends T> expectedLoader) {
        this.expectedLoader = expectedLoader;
        this.descriptionText = descriptionText;
    }

    @Override
    public boolean matches(T actualValue) {
        actual = actualValue;
        expected = Callers.call(expectedLoader);
        
        return expected.equals(actual);
    }


    public String toString() {
        return expected != null ? String.format(descriptionText + " [expected: %s, actual: %s]", expected, actual) : descriptionText;
    }

    public static <E> LazyEqualsPredicate<E> lazyEqualTo(String descriptionText, Callable<? extends E> expectedLoader) {
        return new LazyEqualsPredicate<E>(descriptionText, expectedLoader);
    }
}
