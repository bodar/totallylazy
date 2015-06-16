package com.googlecode.totallylazy;

import static com.googlecode.totallylazy.LazyException.lazyException;

public interface Function2<A, B, C> extends Function1<A, Function1<B, C>> {
     C call(A a, B b) throws Exception;

     @Override
     default Function1<B, C> call(final A a) throws Exception {
          return b -> call(a, b);
     }

     default C apply(final A a, final B b) {
          try {
              return call(a, b);
          } catch (Exception e) {
              throw lazyException(e);
          }
     }

     default Function1<A, C> applySecond(final B b) {
          return flip().apply(b);
     }

     default Function0<C> deferApply(final A a, final B b) {
          return Callables.deferApply(this, a, b);
     }

     default Function2<B, A, C> flip() {
          return Callables.flip(this);
     }

     default Function1<Pair<A, B>, C> pair() {
          return Callables.pair(this);
     }
}
