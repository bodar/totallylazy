package com.googlecode.totallylazy;

import com.googlecode.totallylazy.annotations.multimethod;

public abstract class AbstractTuple extends Eq implements Tuple {
    @Override
    public final String toString() {
        return toString("(", ",", "])");
    }

    @multimethod
    public final boolean equals(final AbstractTuple o) { return values().equals(o.values()); }

    @Override
    public final int hashCode() {
        return values().hashCode();
    }
}
