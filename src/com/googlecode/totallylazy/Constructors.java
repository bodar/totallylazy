package com.googlecode.totallylazy;

import java.lang.reflect.Constructor;
import java.lang.reflect.Type;

public class Constructors {

    public static Function1<Constructor, Type[]> genericParameterTypes() {
        return new Function1<Constructor, Type[]>() {
            public Type[] call(Constructor constructor) throws Exception {
                return constructor.getGenericParameterTypes();
            }
        };
    }
}
