package com.googlecode.totallylazy.matchers;

import com.googlecode.totallylazy.Callers;
import com.googlecode.totallylazy.Function;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.predicates.LogicalPredicate;

import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.Callables.returns;

public final class LazyMatcher<A, E> extends LogicalPredicate<A> {
    private final Function<? super A, ? extends E> actualMapper;
    private final Callable<? extends Predicate<? super E>> expectedMatcher;

    private LazyMatcher(Callable<? extends Predicate<? super E>> expectedMatcher, Function<? super A, ? extends E> actualMapper) {
        this.expectedMatcher = expectedMatcher;
        this.actualMapper = actualMapper;
    }

    @Override
    public boolean matches(A actual) {
        return Callers.call(expectedMatcher).matches(Callers.call(actualMapper, actual));
    }

    public static <A, E> LazyMatcher<A, E> matchesLazily(String descriptionText, E expected, Function<A, E> actualMapper) {
        return new LazyMatcher<A, E>(lazyEqualTo(descriptionText, returns(expected)), actualMapper);
    }

    public static <A, E> LazyMatcher<A, E> matchesLazily(String descriptionText, Callable<? extends E> expected, Function<? super A, ? extends E> actualMapper) {
        return new LazyMatcher<A, E>(lazyEqualTo(descriptionText, expected), actualMapper);
    }

    public static <A, E> LazyMatcher<A, E> matchesLazily(Predicate<? super E> expectedMatcher, Function<? super A, ? extends E> actualMapper) {
        return new LazyMatcher<A, E>(returns(expectedMatcher), actualMapper);
    }

    private static <E> Callable<? extends Predicate<E>> lazyEqualTo(final String descriptionText, final Callable<? extends E> expected) {
        return returns(LazyEqualsPredicate.lazyEqualTo(descriptionText, expected));
    }
}