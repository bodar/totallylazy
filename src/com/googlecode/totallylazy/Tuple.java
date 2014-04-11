package com.googlecode.totallylazy;

public interface Tuple {
    default String toString(String separator) {
        return toString("", separator, "");
    }

    default String toString(String start, String separator, String end) {
        return values().toString(start, separator, end);
    }

    Seq<Object> values();
}
