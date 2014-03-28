package com.googlecode.totallylazy;

public abstract class Block<T> implements Function1<T, Void> {
    @Override
    public Void call(T t) throws Exception {
        execute(t);
        return Runnables.VOID;
    }

    protected abstract void execute(T t) throws Exception;
}
