package com.googlecode.totallylazy;

public interface Function3<A, B, C, D> {
     D call(A a, B b, C c) throws Exception;

     default D apply(final A a, final B b, final C c) {
          return Functions.call(this, a, b, c);
     }

     default Function1<Triple<A, B, C>, D> triple() {
          return Functions.triple(this);
     }
}
