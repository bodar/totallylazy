package com.googlecode.totallylazy.proxy;

public class ThreadLocalInvocation extends ThreadLocal<Invocation>{
    @Override
    public Invocation get() {
        final Invocation result = super.get();
        super.set(null);
        return result;
    }

    @Override
    public void set(Invocation value) {
        if(get() != null){
            throw new UnsupportedOperationException("An unused call already exists, you must use any previous calls before starting another");
        }
        super.set(value);
    }
}
