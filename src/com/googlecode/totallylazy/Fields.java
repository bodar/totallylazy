package com.googlecode.totallylazy;

import java.lang.reflect.Field;

public class Fields {
    public static Mapper<Field, String> name = new Mapper<Field, String>() {
        @Override
        public String call(Field field) throws Exception {
            return field.getName();
        }
    };

    public static Mapper<Field, Class<?>> type = new Mapper<Field, Class<?>>() {
        @Override
        public Class<?> call(Field field) throws Exception {
            return field.getType();
        }
    };

    public static Mapper<Field, Object> value(final Object instance) {
        return new Mapper<Field, Object>() {
            @Override
            public Object call(Field field) throws Exception {
                return access(field).get(instance);
            }
        };
    }

    public static Mapper<Field, Integer> modifiers = new Mapper<Field, Integer>() {
        @Override
        public Integer call(Field field) throws Exception {
            return field.getModifiers();
        }
    };

    public static Field access(Field field) {
        field.setAccessible(true);
        return field;
    }
}
