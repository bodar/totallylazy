package com.googlecode.totallylazy.predicates;

import com.googlecode.totallylazy.Predicate;

public class ModRemainderIs implements Predicate<Integer> {
    private final int mod;
    private final int remainder;

    public ModRemainderIs(int mod, int remainder) {
        this.mod = mod;
        this.remainder = remainder;
    }

    public boolean matches(Integer other) {
        return other % mod == remainder;
    }
}
