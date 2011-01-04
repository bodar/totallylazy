package com.googlecode.totallylazy;

public interface Callable1<Input, Output> {
     Output call(Input input) throws Exception;
}
