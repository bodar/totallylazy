package com.googlecode.totallylazy;

import static com.googlecode.totallylazy.LazyException.lazyException;

public interface Function4<A, B, C, D, E> extends Function3<A,B,C,Function1<D,E>>{
     E call(A a, B b, C c, D d) throws Exception;

     @Override
     default Function1<D, E> call(final A a, final B b, final C c) throws Exception {
          return Functions.<A, B, C, D, E>apply(this, a).apply(b).apply(c);
     }

     default E apply(final A a, final B b, final C c, final D d){
          try {
              return call(a, b, c, d);
          } catch (Exception e) {
              throw lazyException(e);
          }
     }

     default Function1<Quadruple<A, B, C, D>, E> quadruple() {
          return Functions.quadruple(this);
     }
}
