package com.googlecode.totallylazy.proxy;

import com.googlecode.totallylazy.Mapper;
import com.googlecode.totallylazy.Unchecked;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static com.googlecode.totallylazy.Sequences.sequence;

public class FieldInvocation<A, B> extends Mapper<A, B> implements Invocation<A, B> {
    private final Field field;

    public FieldInvocation(Field field) {
        this.field = field;
    }

    @Override
    public String toString() {
        return field.getName();
    }

    @Override
    public B call(A instance) throws InvocationTargetException, IllegalAccessException {
        return Unchecked.cast(field.get(instance));
    }
}
