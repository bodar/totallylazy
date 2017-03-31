package com.googlecode.totallylazy.reflection;

import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.functions.Lazy;
import com.googlecode.totallylazy.predicates.LogicalPredicate;
import sun.reflect.ReflectionFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import static com.googlecode.totallylazy.Exceptions.optional;
import static com.googlecode.totallylazy.Unchecked.cast;
import static com.googlecode.totallylazy.functions.Lazy.lazy;
import static com.googlecode.totallylazy.predicates.Predicates.and;
import static com.googlecode.totallylazy.reflection.Fields.name;
import static com.googlecode.totallylazy.predicates.Predicates.is;
import static com.googlecode.totallylazy.predicates.Predicates.where;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Strings.startsWith;
import static com.googlecode.totallylazy.reflection.Methods.allMethods;
import static com.googlecode.totallylazy.reflection.Methods.invokeOn;
import static com.googlecode.totallylazy.reflection.Methods.modifier;
import static com.googlecode.totallylazy.reflection.Methods.returnType;
import static com.googlecode.totallylazy.reflection.StackFrames.stackFrames;
import static com.googlecode.totallylazy.reflection.Types.matches;
import static java.lang.String.format;
import static java.lang.reflect.Modifier.STATIC;

public class Reflection {
    private final static ReflectionFactory reflectionFactory = ReflectionFactory.getReflectionFactory();
    private final static Lazy<Constructor<?>> constructor = lazy(() -> Object.class.getConstructor((Class[]) null));

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

    private static void map(Class<?> primitive, Class<?> boxed) {
        if (!primitive.isPrimitive())
            throw new IllegalArgumentException("Class must be primitive but was " + primitive);
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

    public static Class<?> box(Class<?> primitive) {
        if (!primitive.isPrimitive())
            throw new IllegalArgumentException("Class must be primitive but was " + primitive);
        return primitiveToBoxed.get(primitive);
    }

    public static Class<?> unbox(Class<?> boxed) {
        Class<?> aClass = boxedToPrimative.get(boxed);
        if (aClass == null) throw new IllegalArgumentException("Class must be boxed type but was " + boxed);
        return aClass;
    }

    /**
     * Creates object by calling any constructor (private or otherwise)
     **/
    public static <T> T newInstance(Class<T> recordType, Object... args) throws Exception {
        Constructor<T> constructor = recordType.getDeclaredConstructor(sequence(args).map(Object::getClass).toArray(Class.class));
        constructor.setAccessible(true);
        return constructor.newInstance(args);
    }

    /**
     * Evil creates object without calling constructors or field initialises
     **/
    public static <T> T create(Class aClass) throws ReflectiveOperationException {
        Constructor<?> constructor = reflectionFactory.newConstructorForSerialization(aClass, Reflection.constructor.value());
        return cast(constructor.newInstance());
    }

    public static Class<?> defineClass(ClassLoader classLoader, String name, byte[] bytes) {
        return Methods.allMethods(classLoader.getClass()).
                filter(m -> m.getName().equals("defineClass")).
                find(m -> sequence(m.getParameterTypes()).equals(sequence(String.class, byte[].class, int.class, int.class))).
                map(m -> {
                    m.setAccessible(true);
                    return (Class<?>) m.invoke(classLoader, name, bytes, 0, bytes.length);
                }).get();
    }

    /**
     * Finds a static method on actualType that takes a parameter of the same type as value and return
     * an instance of actualType.
     * <p>
     * Calls that static method with value and returns the result.
     **/
    public static <T> T valueOf(Class<T> actualType, Object value) {
        Sequence<Method> candidates = allMethods(actualType)
                .filter(and(modifier(STATIC),
                        where(m -> m.getParameterTypes().length, is(1)))).
                        filter(and(
                                where(returnType(), matches(actualType)),
                                where(m -> m.getParameterTypes()[0], (t) -> t.isAssignableFrom(value.getClass()))));

        if (candidates.isEmpty()) {
            throw new NoSuchElementException(format(
                    "Cannot create %s from '%s' (%s). You need to add a static constructor method to %s that accepts a %s",
                    actualType.getCanonicalName(), value, value.getClass().getCanonicalName(),
                    actualType.getSimpleName(), value.getClass().getSimpleName()));
        }

        return actualType.cast(candidates.pick(optional(invokeOn(null, value))));
    }

}
