package com.googlecode.totallylazy.reflection;

import com.googlecode.totallylazy.functions.Lazy;
import com.googlecode.totallylazy.predicates.LogicalPredicate;
import sun.reflect.ReflectionFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static com.googlecode.totallylazy.Unchecked.cast;
import static com.googlecode.totallylazy.reflection.Fields.name;
import static com.googlecode.totallylazy.predicates.Predicates.is;
import static com.googlecode.totallylazy.predicates.Predicates.where;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Strings.startsWith;
import static com.googlecode.totallylazy.reflection.StackFrames.stackFrames;

public class Reflection {
    private final static ReflectionFactory reflectionFactory = ReflectionFactory.getReflectionFactory();
    private final static Lazy<Constructor<?>> constructor = Lazy.lazy(() -> Object.class.getConstructor((Class[]) null));

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

    private static Map<Class<?>, Class<?>> primitiveToBoxed = new HashMap<>();
    private static Map<Class<?>, Class<?>> boxedToPrimative = new HashMap<>();
    private static void map(Class<?> primitive, Class<?> boxed){
        if(!primitive.isPrimitive()) throw new IllegalArgumentException("Class must be primitive but was " + primitive);
        primitiveToBoxed.put(primitive, boxed);
        boxedToPrimative.put(boxed, primitive);
    }

    static {
        map(void.class, Void.class);
        map(char.class, Character.class);
        map(boolean.class, Boolean.class);
        map(byte.class, Byte.class);
        map(short.class, Short.class);
        map(int.class, Integer.class);
        map(long.class, Long.class);
        map(float.class, Float.class);
        map(double.class, Double.class);
    }

    public static Class<?> box(Class<?> primitive){
        if(!primitive.isPrimitive()) throw new IllegalArgumentException("Class must be primitive but was " + primitive);
        return primitiveToBoxed.get(primitive);
    }

    public static Class<?> unbox(Class<?> boxed){
        Class<?> aClass = boxedToPrimative.get(boxed);
        if(aClass == null) throw new IllegalArgumentException("Class must be boxed type but was " + boxed);
        return aClass;
    }

    public static <T> T create(Class aClass) throws ReflectiveOperationException {
        Constructor<?> constructor = reflectionFactory.newConstructorForSerialization(aClass, Reflection.constructor.value());
        return cast(constructor.newInstance());
    }
}
