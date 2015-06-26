package com.googlecode.totallylazy.functions;

public enum Xor implements CurriedMonoid<Boolean> {
    instance;
    
    @Override
    public Boolean call(Boolean a, Boolean b) throws Exception {
        return a ^ b;
    }

    @Override
    public Boolean identity() {
        return false;
    }
}
