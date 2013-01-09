package com.googlecode.totallylazy;

import java.lang.reflect.Field;

public class Fields {
    public static Mapper<Field, String> name = new Mapper<Field, String>() {
        @Override
        public String call(Field field) throws Exception {
            return field.getName();
        }
    };
    public static Mapper<Field, Integer> modifiers = new Mapper<Field, Integer>() {
        @Override
        public Integer call(Field field) throws Exception {
            return field.getModifiers();
        }
    };
}
