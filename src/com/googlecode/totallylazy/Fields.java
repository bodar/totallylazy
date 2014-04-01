package com.googlecode.totallylazy;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static com.googlecode.totallylazy.Classes.allClasses;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Unchecked.cast;

public class Fields {
    public static Function<Field, String> name = new Function<Field, String>() {
        @Override
        public String call(Field field) throws Exception {
            return field.getName();
        }
    };

    public static Function<Field, Class<?>> type = new Function<Field, Class<?>>() {
        @Override
        public Class<?> call(Field field) throws Exception {
            return field.getType();
        }
    };

    public static <T> Function<Field, T> value(final Object instance) {
        return new Function<Field, T>() {
            @Override
            public T call(Field field) throws Exception {
                return get(field, instance);
            }
        };
    }

    public static <T> Function<Field, T> value(final Object instance, final Class<T> aClass) {
        return new Function<Field, T>() {
            @Override
            public T call(Field field) throws Exception {
                return get(field, instance, aClass);
            }
        };
    }

    public static <T> T get(Field field, Object instance) throws IllegalAccessException {
        return cast(access(field).get(instance));
    }

    public static <T> T get(Field field, Object instance, Class<T> aClass) throws IllegalAccessException {
        return aClass.cast(access(field).get(instance));
    }

    public static Function<Field, Integer> modifiers = new Function<Field, Integer>() {
        @Override
        public Integer call(Field field) throws Exception {
            return field.getModifiers();
        }
    };

    public static Field access(Field field) {
        field.setAccessible(true);
        return field;
    }

    public static Sequence<Field> fields(Class<?> aClass) {
        return allClasses(aClass).flatMap(Fields.fields());
    }

    public static Function<Class<?>, Sequence<Field>> fields() {
        return new Function<Class<?>, Sequence<Field>>() {
            public Sequence<Field> call(Class<?> aClass) throws Exception {
                return sequence(aClass.getDeclaredFields());
            }
        };
    }
}
