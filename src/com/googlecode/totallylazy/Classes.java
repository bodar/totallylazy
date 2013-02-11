package com.googlecode.totallylazy;

import com.googlecode.totallylazy.predicates.LogicalPredicate;

import static com.googlecode.totallylazy.Sequences.empty;
import static com.googlecode.totallylazy.Sequences.sequence;

public class Classes {
    public static LogicalPredicate<Class<?>> isInstance(final Object instance) {
        return new LogicalPredicate<Class<?>>() {
            public boolean matches(Class<?> aClass) {
                return aClass.isInstance(instance);
            }
        };
    }

    public static  Sequence<Class<?>> allClasses(Class<?> aClass) {
        if(aClass == null) return empty();
        return sequence(aClass.getInterfaces()).
                join(allClasses(aClass.getSuperclass())).
                cons(aClass);
    }
}
