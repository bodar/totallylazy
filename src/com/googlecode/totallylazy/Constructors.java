package com.googlecode.totallylazy;

import java.lang.reflect.Constructor;
import java.lang.reflect.Type;

public class Constructors {

    public static Function1<Constructor, Type[]> genericParameterTypes() {
        return Constructor::getGenericParameterTypes;
    }
}
