package com.googlecode.totallylazy;

import java.lang.reflect.Constructor;
import java.lang.reflect.Type;

public class Constructors {

    public static Callable1<Constructor, Type[]> genericParameterTypes() {
        return new Callable1<Constructor, Type[]>() {
            public Type[] call(Constructor constructor) throws Exception {
                return constructor.getGenericParameterTypes();
            }
        };
    }
}
