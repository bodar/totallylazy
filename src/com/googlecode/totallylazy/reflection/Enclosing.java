package com.googlecode.totallylazy.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public abstract class Enclosing {
    public Method method(){
        return getClass().getEnclosingMethod();
    }

    public Constructor<?> constructor(){
        return getClass().getEnclosingConstructor();
    }
}
