package com.googlecode.totallylazy.predicates;

import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.proxy.Generics;

public abstract class TypeSafePredicate<T> implements Predicate<T> {
    private final Class<T> expectedClass;

    protected TypeSafePredicate() {
        this.expectedClass = Generics.getGenericSuperclassType(getClass(), 0);
    }

    protected TypeSafePredicate(Class<T> expectedClass) {
        this.expectedClass = expectedClass;
    }

    public boolean matches(T other) {
        return isTheRightClass(other) && matchesSafely(other);
    }

    private boolean isTheRightClass(T other) {
        return other == null || expectedClass.isAssignableFrom(other.getClass());
    }

    protected abstract boolean matchesSafely(T other);
}
