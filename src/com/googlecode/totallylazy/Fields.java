package com.googlecode.totallylazy;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static com.googlecode.totallylazy.Classes.allClasses;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Predicates.not;
import static com.googlecode.totallylazy.Predicates.where;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Unchecked.cast;

public class Fields {
    public static Function1<Field, String> name = new Function1<Field, String>() {
        @Override
        public String call(Field field) throws Exception {
            return field.getName();
        }
    };

    public static Function1<Field, Class<?>> type = new Function1<Field, Class<?>>() {
        @Override
        public Class<?> call(Field field) throws Exception {
            return field.getType();
        }
    };

    public static <T> Function1<Field, T> value(final Object instance) {
        return new Function1<Field, T>() {
            @Override
            public T call(Field field) throws Exception {
                return get(field, instance);
            }
        };
    }

    public static <T> Function1<Field, T> value(final Object instance, final Class<T> aClass) {
        return new Function1<Field, T>() {
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

    public static Function1<Field, Integer> modifiers = new Function1<Field, Integer>() {
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

    public static Function1<Class<?>, Sequence<Field>> fields() {
        return new Function1<Class<?>, Sequence<Field>>() {
            public Sequence<Field> call(Class<?> aClass) throws Exception {
                return sequence(aClass.getDeclaredFields());
            }
        };
    }

    public static Sequence<Field> syntheticFields(Class<?> aClass) {
        return sequence(aClass.getDeclaredFields()).
                filter(where(modifiers, is(Reflection.synthetic)));
    }

    public static Sequence<Field> nonSyntheticFields(Class<?> aClass) {
        return sequence(aClass.getDeclaredFields()).
                filter(where(modifiers, not(Reflection.synthetic)));
    }
}
