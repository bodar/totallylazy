package com.googlecode.totallylazy.functions;

import com.googlecode.totallylazy.Pair;

public interface Function2<A, B, C> {
     C call(A a, B b) throws Exception;

     default C apply(final A a, final B b) {
          return Functions.call(this, a, b);
     }

     default Function2<B, A, C> flip() {
          return Callables.flip(this);
     }

     default Function1<Pair<A, B>, C> pair() {
          return Callables.pair(this);
     }

     default Function1<A, C> applySecond(final B b) {
          return Callables.flip(this).apply(b);
     }

     default Function0<C> deferApply(final A a, final B b) {
          return Callables.deferApply(this, a, b);
     }
}
