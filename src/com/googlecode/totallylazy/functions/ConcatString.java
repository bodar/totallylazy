package com.googlecode.totallylazy.functions;

import com.googlecode.totallylazy.Strings;

public enum ConcatString implements CurriedMonoid<String> {
    instance;

    @Override
    public String call(String a, String b) throws Exception {
        return a + b;
    }

    @Override
    public String identity() {
        return Strings.EMPTY;
    }

    @Override
    public String toString() {
        return "join";
    }
}
