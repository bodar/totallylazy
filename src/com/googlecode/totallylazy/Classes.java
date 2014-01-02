package com.googlecode.totallylazy;

import com.googlecode.totallylazy.predicates.LogicalPredicate;

import java.io.File;

import static com.googlecode.totallylazy.Option.none;
import static com.googlecode.totallylazy.Sequences.empty;
import static com.googlecode.totallylazy.Sequences.sequence;

public class Classes {
    public static Option<Class<?>> forName(final String name) {
        try {
            return Option.<Class<?>>some(Class.forName(name));
        } catch (Exception e) {
            return none();
        }
    }

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

    public static String filename(Class<?> aClass) {
        return filename(aClass.getName());
    }

    public static String filename(String className) {
        return className.replace('.', '/') + ".class";
    }

    public static byte[] bytes(Class<?> aClass){
        return Bytes.bytes(aClass.getResourceAsStream("/" + filename(aClass)));
    }

}
