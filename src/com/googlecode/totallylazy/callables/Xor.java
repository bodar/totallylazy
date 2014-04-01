package com.googlecode.totallylazy.callables;

import com.googlecode.totallylazy.Combiner;

public class Xor implements Combiner<Boolean> {
    @Override
    public Boolean call(Boolean a, Boolean b) throws Exception {
        return a ^ b;
    }

    @Override
    public Boolean identityElement() {
        return false;
    }
}
