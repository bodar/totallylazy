package com.googlecode.totallylazy;

import static com.googlecode.totallylazy.Sequences.*;

public class Options {
    public static <T> Sequence<T> flatten(Iterable<Option<T>> items) {
        return sequence(items).filter(Option::isDefined).map(Option::get);
    }
}
