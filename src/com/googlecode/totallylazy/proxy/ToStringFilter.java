package com.googlecode.totallylazy.proxy;

import net.sf.cglib.proxy.CallbackFilter;

import java.lang.reflect.Method;

public class ToStringFilter implements CallbackFilter {
    public int accept(Method method) {
        return method.getName().equals("toString") ? 1 : 0;
    }
}
