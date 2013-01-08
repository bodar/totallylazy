package com.googlecode.totallylazy;

import java.lang.reflect.Field;

import static com.googlecode.totallylazy.Predicates.where;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Strings.startsWith;

public class Reflection {
    public static Object enclosingInstance(Object innerClass) {
        try {
            Field field = sequence(innerClass.getClass().getDeclaredFields()).find(where(Fields.name, startsWith("this$"))).get();
            field.setAccessible(true);
            return field.get(innerClass);
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
    }
}
