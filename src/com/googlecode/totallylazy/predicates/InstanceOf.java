package com.googlecode.totallylazy.predicates;

import com.googlecode.totallylazy.Predicate;

public class InstanceOf<T> implements Predicate<T> {
    private final Class aClass;

    public InstanceOf(Class aClass) {
        this.aClass = aClass;
    }

    public boolean matches(T other) {
        if(other == null) {
            return false;
        }
        return aClass.isAssignableFrom(other.getClass());
    }
}
