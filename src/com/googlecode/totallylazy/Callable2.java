package com.googlecode.totallylazy;

public interface Callable2<A, B, C> {
     C call(A a, B b) throws Exception;
}
