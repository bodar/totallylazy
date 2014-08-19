package com.googlecode.totallylazy;

import com.googlecode.totallylazy.predicates.LogicalPredicate;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

import static com.googlecode.totallylazy.Fields.access;
import static com.googlecode.totallylazy.Fields.name;
import static com.googlecode.totallylazy.Fields.syntheticFields;
import static com.googlecode.totallylazy.LazyException.lazyException;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Predicates.where;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Strings.startsWith;
import static java.util.Collections.synchronizedMap;

public class Reflection {
    public static LogicalPredicate<Integer> synthetic = new LogicalPredicate<Integer>() {
        @Override
        public boolean matches(Integer mod) {
            return (mod & 0x00001000) != 0;
        }
    };

    private static Map<Class, Field> enclosing = synchronizedMap(new WeakHashMap<>());

    public static Object enclosingInstance(Object innerClass) {
        try {
            Field field = enclosing.computeIfAbsent(innerClass.getClass(), c -> syntheticFields(c).
                    find(where(name, startsWith("this$"))).
                    get());
            return access(field).get(innerClass);
        } catch (Exception e) {
            throw lazyException(e);
        }
    }
}
