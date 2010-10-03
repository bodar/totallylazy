package com.googlecode.totallylazy.proxy;

import java.util.ArrayList;
import java.util.List;

public class ThreadLocalInvocations extends ThreadLocal{
    @Override
    protected Object initialValue() {
        return new ArrayList<Invocation>();
    }

    public List<Invocation> list() {
        return (List<Invocation>) get();
    }

    public void add(Invocation invocation) {
        list().add(invocation);
    }

    public Invocation pop() {
        Invocation invocation = list().remove(0);
        list().clear();
        return invocation;
    }
}
