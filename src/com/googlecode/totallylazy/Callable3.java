package com.googlecode.totallylazy;

public interface Callable3<A, B, C, D> {
     D call(A a, B b, C c) throws Exception;
}
