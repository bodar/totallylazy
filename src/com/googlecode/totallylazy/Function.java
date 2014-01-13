package com.googlecode.totallylazy;

public interface Function<Input, Output> {
     Output call(Input input) throws Exception;
}