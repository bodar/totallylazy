package com.googlecode.totallylazy.proxy;

public class ThreadLocalInvocation<T,R> extends ThreadLocal<Invocation<T,R>>{
    @Override
    public Invocation<T,R> get() {
        final Invocation<T,R> result = super.get();
        super.set(null);
        return result;
    }

    @Override
    public void set(Invocation<T,R> value) {
        if(get() != null){
            throw new UnsupportedOperationException("An unused call already exists, you must use any previous calls before starting another");
        }
        super.set(value);
    }
}
