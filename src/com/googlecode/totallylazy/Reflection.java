package com.googlecode.totallylazy;

import java.lang.reflect.Field;

import static com.googlecode.totallylazy.Fields.modifiers;
import static com.googlecode.totallylazy.Fields.name;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Predicates.where;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Strings.startsWith;

public class Reflection {
    public static Predicate<Integer> synthetic = mod -> (mod & 0x00001000) != 0;

    public static Object enclosingInstance(Object innerClass) {
        try {
            Field field = syntheticFields(innerClass.getClass()).
                    find(where(name, startsWith("this$"))).
                    get();
            field.setAccessible(true);
            return field.get(innerClass);
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
    }

    public static Sequence<Field> syntheticFields(Class<?> aClass) {
        return sequence(aClass.getDeclaredFields()).
                filter(where(modifiers, is(synthetic)));
    }
}
