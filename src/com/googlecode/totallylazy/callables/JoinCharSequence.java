package com.googlecode.totallylazy.callables;

import com.googlecode.totallylazy.Combiner;
import com.googlecode.totallylazy.Function2;
import com.googlecode.totallylazy.Strings;

public class JoinCharSequence extends Function2<CharSequence, CharSequence, CharSequence> implements Combiner<CharSequence> {
    @Override
    public CharSequence call(CharSequence a, CharSequence b) throws Exception {
        return new StringBuilder(a).append(b);
    }

    @Override
    public CharSequence identity() {
        return Strings.EMPTY;
    }
}
