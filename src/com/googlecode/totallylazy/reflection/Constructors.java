package com.googlecode.totallylazy.reflection;

import com.googlecode.totallylazy.functions.Function1;

import java.lang.reflect.Constructor;
import java.lang.reflect.Type;

public class Constructors {
    public static Function1<Constructor, Type[]> genericParameterTypes() {
        return Constructor::getGenericParameterTypes;
    }
}
