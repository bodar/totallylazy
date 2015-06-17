package com.googlecode.totallylazy;

public abstract class Block<T> extends Function<T, Void> {
    @Override
    public Void call(T t) throws Exception {
        execute(t);
        return Runnables.VOID;
    }

    protected abstract void execute(T t) throws Exception;
}
