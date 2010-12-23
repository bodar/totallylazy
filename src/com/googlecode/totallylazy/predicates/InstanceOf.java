package com.googlecode.totallylazy.predicates;

public class InstanceOf<T> extends LogicalPredicate<T> {
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
