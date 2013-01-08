package com.googlecode.totallylazy;

import java.lang.reflect.Field;

public class Fields {
    public static Mapper<Field, String> name = new Mapper<Field, String>() {
        @Override
        public String call(Field field) throws Exception {
            return field.getName();
        }
    };
}
