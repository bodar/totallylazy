package com.googlecode.totallylazy.matchers;

import com.googlecode.totallylazy.Callable1;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.Callables.returns;
import static com.googlecode.totallylazy.Callers.call;

public final class LazyMatcher<A, E> extends TypeSafeMatcher<A> {
    private final Callable1<A,E> actualMapper;
    private final Callable<? extends Matcher<? super E>> expectedMatcher;

    private LazyMatcher(Callable<? extends Matcher<? super E>> expectedMatcher, Callable1<A, E> actualMapper) {
        this.expectedMatcher = expectedMatcher;
        this.actualMapper = actualMapper;
    }

    @Override
    protected boolean matchesSafely(A actual) {
        return call(expectedMatcher).matches(call(actualMapper, actual));
    }

    public void describeTo(Description description) {
        call(expectedMatcher).describeTo(description);
    }

    public static <A, E> LazyMatcher<A, E> matchesLazily(String descriptionText, E expected, Callable1<A, E> actualMapper) {
        return new LazyMatcher<A, E>(lazyEqualTo(descriptionText, returns(expected)), actualMapper);
    }

    public static <A, E> LazyMatcher<A, E> matchesLazily(String descriptionText, Callable<E> expected, Callable1<A, E> actualMapper) {
        return new LazyMatcher<A, E>(lazyEqualTo(descriptionText, expected), actualMapper);
    }

    public static <A, E> LazyMatcher<A, E> matchesLazily(Matcher<? super E> expectedMatcher, Callable1<A, E> actualMapper) {
        return new LazyMatcher<A, E>(returns(expectedMatcher), actualMapper);
    }

    private static <E> Callable<? extends Matcher<E>> lazyEqualTo(final String descriptionText, final Callable<E> expected) {
        return returns(LazyEqualsMatcher.lazyEqualTo(descriptionText, expected));
    }
}