package com.googlecode.totallylazy;

import java.util.Iterator;

import static com.googlecode.totallylazy.Sequences.sequence;

public class Pair<F, S> implements Iterable, First<F>, Second<S> {
    private final F first;
    private final S second;

    public static <F, S> Pair<F, S> pair(F first, S second) {
        return new Pair<F, S>(first, second);
    }

    private Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }

    public F first() {
        return first;
    }

    public S second() {
        return second;
    }

    public Iterator iterator() {
        return sequence(first, second).iterator();
    }

    @Override
    public String toString() {
        return sequence(first, second).toString("[", ",", "]");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pair pair = (Pair) o;

        if (first != null ? !first.equals(pair.first) : pair.first != null) return false;
        if (second != null ? !second.equals(pair.second) : pair.second != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = first != null ? first.hashCode() : 0;
        result = 31 * result + (second != null ? second.hashCode() : 0);
        return result;
    }
}
