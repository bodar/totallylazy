package com.googlecode.totallylazy.predicates;

import java.lang.annotation.Annotation;

public class InstanceOf<T> extends LogicalPredicate<T> {
    private final Class aClass;

    public InstanceOf(Class aClass) {
        this.aClass = aClass;
    }

    public boolean matches(T other) {
        if(other == null) {
            return false;
        }
        if(other instanceof Annotation){
            return ((Annotation) other).annotationType().equals(aClass);
        }
        return aClass.isInstance(other);
    }
}
