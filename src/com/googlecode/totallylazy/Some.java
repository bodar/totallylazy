package com.googlecode.totallylazy;

import java.util.Iterator;

import static com.googlecode.totallylazy.Sequences.sequence;

public class Some<T> extends Option<T> {
    private final T t;

    public Some(T t) {
        this.t = t;
    }

    public static <T> Some<T> some(T t) {
        return new Some<T>(t);
    }

    @SuppressWarnings("unchecked")
    public Iterator<T> iterator() {
        return sequence(t).iterator();
    }

    @Override
    public T get() {
        return t;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Some some = (Some) o;

        if (t != null ? !t.equals(some.t) : some.t != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return t != null ? t.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "some(" + t + ")";
    }
}
