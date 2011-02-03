package com.googlecode.totallylazy;

import com.googlecode.totallylazy.predicates.LogicalPredicate;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

public class Constructors {

    public static Callable1<? super Constructor, Type[]> genericParameterTypes() {
        return new Callable1<Constructor, Type[]>() {
            public Type[] call(Constructor constructor) throws Exception {
                return constructor.getGenericParameterTypes();
            }
        };
    }
}
