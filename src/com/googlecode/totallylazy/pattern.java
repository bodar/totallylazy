package com.googlecode.totallylazy;

import java.lang.reflect.Method;

import static com.googlecode.totallylazy.Predicates.any;

public abstract class pattern {
    private final Object[] argument;

    public pattern(Object... argument) {
        this.argument = argument;
    }

    public <T> T match() { return new multi(any(Method.class)){}.method(argument); }
    public <T> Option<T> matchOption() { return new multi(any(Method.class)){}.methodOption(argument); }
}
