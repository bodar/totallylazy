package com.googlecode.totallylazy.functions;

import com.googlecode.totallylazy.CurriedMonoid;

public class Xor implements CurriedMonoid<Boolean> {
    @Override
    public Boolean call(Boolean a, Boolean b) throws Exception {
        return a ^ b;
    }

    @Override
    public Boolean identity() {
        return false;
    }
}
