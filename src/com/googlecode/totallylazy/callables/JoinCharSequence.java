package com.googlecode.totallylazy.callables;

import com.googlecode.totallylazy.CombinerFunction;
import com.googlecode.totallylazy.Strings;

public class JoinCharSequence extends CombinerFunction<CharSequence> {
    @Override
    public CharSequence call(CharSequence a, CharSequence b) throws Exception {
        return new StringBuilder(a).append(b);
    }

    @Override
    public CharSequence identity() {
        return Strings.EMPTY;
    }
}
