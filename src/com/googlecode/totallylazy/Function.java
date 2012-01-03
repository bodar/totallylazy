package com.googlecode.totallylazy;

import java.util.concurrent.Callable;

public abstract class Function<T> implements Callable<T>, Runnable{
    public T apply(){
        return Callers.call(this);
    }

    @Override
    public void run() {
        apply();
    }
}