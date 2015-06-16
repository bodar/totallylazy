package com.googlecode.totallylazy.callables;

import com.googlecode.totallylazy.Monoid;

public class And implements Monoid<Boolean> {
    @Override
    public Boolean call(Boolean a, Boolean b) throws Exception {
        return a && b;
    }

    @Override
    public Boolean identity() {
        return true;
    }
}
