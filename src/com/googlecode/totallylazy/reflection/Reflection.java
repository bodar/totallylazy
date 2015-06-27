package com.googlecode.totallylazy.reflection;

import com.googlecode.totallylazy.predicates.LogicalPredicate;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static com.googlecode.totallylazy.reflection.Fields.name;
import static com.googlecode.totallylazy.predicates.Predicates.is;
import static com.googlecode.totallylazy.predicates.Predicates.where;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Strings.startsWith;
import static com.googlecode.totallylazy.reflection.StackFrames.stackFrames;

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

    public static StackFrame enclosingFrame() {
        return stackFrames().tail().head();
    }

    public static Method enclosingMethod() {
        return stackFrames().tail().head().method();
    }

    public static Constructor<?> enclosingConstructor() {
        return stackFrames().tail().head().constructor();
    }

    public static StackFrame callingFrame() {
        return stackFrames().drop(2).head();
    }

    public static Method callingMethod() {
        return stackFrames().drop(2).head().method();
    }

    public static Constructor<?> callingConstructor() {
        return stackFrames().drop(2).head().constructor();
    }

    public static Class<?> callingClass() {
        return stackFrames().drop(2).head().aClass();
    }

}
