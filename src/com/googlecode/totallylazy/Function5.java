package com.googlecode.totallylazy;

import static com.googlecode.totallylazy.LazyException.lazyException;

public interface Function5<A, B, C, D, E, F> extends Function4<A,B,C,D,Function1<E,F>> {
     F call(A a, B b, C c, D d, E e) throws Exception;

     @Override
     default Function1<E, F> call(final A a, final B b, final C c, final D d) throws Exception {
          return Functions.<A, B, C, D, E, F>apply(this, a).apply(b).apply(c).apply(d);
     }

     default F apply(final A a, final B b, final C c, final D d, final E e) {
          try {
              return call(a, b, c, d, e);
          } catch (Exception ex) {
              throw lazyException(ex);
          }
     }

     default Function1<Quintuple<A, B, C, D, E>, F> quintuple() {
          return Functions.quintuple(this);
     }
}
