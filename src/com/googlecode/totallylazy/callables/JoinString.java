package com.googlecode.totallylazy.callables;

import com.googlecode.totallylazy.Monoid;
import com.googlecode.totallylazy.Strings;

public class JoinString implements Monoid<String> {
    public static final Monoid<String> instance = new JoinString();
    private JoinString() {}

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
