package com.googlecode.totallylazy.callables;

import com.googlecode.totallylazy.Function2;
import com.googlecode.totallylazy.Identity;

public class And extends Function2<Boolean, Boolean, Boolean> implements Identity<Boolean> {
    @Override
    public Boolean call(Boolean a, Boolean b) throws Exception {
        return a && b;
    }

    @Override
    public Boolean identity() {
        return true;
    }
}
