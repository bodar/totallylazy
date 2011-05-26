package com.googlecode.totallylazy;

import com.googlecode.totallylazy.predicates.LogicalPredicate;

public class Classes {
    public final LogicalPredicate<Class<?>> isInstance(final Object instance) {
        return new LogicalPredicate<Class<?>>() {
            public boolean matches(Class<?> aClass) {
                return aClass.isInstance(instance);
            }
        };
    }
}
