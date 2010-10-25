package com.googlecode.totallylazy.sql;

public interface Record {
    <T> T get(Keyword<T> keyword) throws Exception;
}
