package com.googlecode.totallylazy;

import static com.googlecode.totallylazy.Sequences.sequence;

public class Pair<F, S> implements First<F>, Second<S> {
    private final F first;
    private final S second;

    public static <F, S> Pair<F, S> pair(F first, S second) {
        return new Pair<F, S>(first, second);
    }

    public Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }

    public final F first() {
        return first;
    }

    public final S second() {
        return second;
    }

    @Override
    public String toString() {
        return toString("[", ",", "]");
    }

    public String toString(String separator) {
        return toString("", separator, "");
    }

    public String toString(String start, String separator, String end){
        return sequence(first, second).toString(start, separator, end);
    }

    @Override
    public boolean equals(final Object o) {
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
