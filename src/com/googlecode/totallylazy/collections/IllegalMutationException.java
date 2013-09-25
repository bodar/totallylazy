package com.googlecode.totallylazy.collections;

public class IllegalMutationException extends UnsupportedOperationException {
    public IllegalMutationException() {
        super("Does not support mutation");
    }
}
