package com.googlecode.totallylazy;

import com.googlecode.totallylazy.predicates.LogicalPredicate;

import java.lang.reflect.Field;

import static com.googlecode.totallylazy.Fields.name;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Predicates.where;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Strings.startsWith;

public class Reflection {
    public static LogicalPredicate<Integer> synthetic = new LogicalPredicate<Integer>() {
        @Override
        public boolean matches(Integer mod) {
            return (mod & 0x00001000) != 0;
        }
    };

    public static Object enclosingInstance(Object innerClass) {
        try {
            Field field = Fields.syntheticFields(innerClass.getClass()).
                    find(where(name, startsWith("this$"))).
                    get();
            field.setAccessible(true);
            return field.get(innerClass);
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
    }

}
