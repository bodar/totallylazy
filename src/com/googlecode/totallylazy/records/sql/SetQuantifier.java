package com.googlecode.totallylazy.records.sql;

public enum SetQuantifier {
    DISTINCT,
    ALL;

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
