package com.googlecode.totallylazy.proxy;

import com.googlecode.totallylazy.reflection.Fields;
import com.googlecode.totallylazy.Function1;
import com.googlecode.totallylazy.Unchecked;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import static com.googlecode.totallylazy.Sequences.sequence;

public class FieldInvocation<A, B> implements Function1<A, B>, Invocation<A, B> {
    private final Field field;

    public FieldInvocation(Field field) {
        this.field = field;
    }

    public Field field() {
        return field;
    }

    @Override
    public String toString() {
        return field.getName();
    }

    @Override
    public B call(A instance) throws InvocationTargetException, IllegalAccessException {
        return Unchecked.cast(Fields.get(field, instance));
    }
}
