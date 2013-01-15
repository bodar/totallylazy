package com.googlecode.totallylazy.callables;

import com.googlecode.totallylazy.CombinerFunction;
import com.googlecode.totallylazy.Strings;

public class JoinCharSequence extends CombinerFunction<CharSequence> {
    public static final CombinerFunction<CharSequence> instance = new JoinCharSequence();
    private JoinCharSequence() {}

    @Override
    public CharSequence call(CharSequence a, CharSequence b) throws Exception {
        return builder(a).append(b);
    }

    private StringBuilder builder(CharSequence a) {
        if(a instanceof StringBuilder) return (StringBuilder) a;
        return new StringBuilder(a);
    }

    @Override
    public CharSequence identity() {
        return Strings.EMPTY;
    }

    @Override
    public String toString() {
        return "join";
    }
}
