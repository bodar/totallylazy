package com.googlecode.totallylazy;

@FunctionalInterface
public interface Function2<A, B, C> extends Function<A, Function<B, C>> {
    C call(A a, B b) throws Exception;

    @Override
    default Function<B, C> call(final A a) throws Exception {
        return Functions.apply(this, a);
    }

    default C apply(final A a, final B b) {
        return Functions.call(this, a, b);
    }

    default Function<A, C> applySecond(final B b) {
        return flip().apply(b);
    }

    default Returns<C> deferApply(final A a, final B b) {
        return Callables.deferApply(this, a, b);
    }

    default Function2<B, A, C> flip() {
        return Callables.flip(this);
    }

    default Function<Pair<A, B>, C> pair() {
        return Callables.pair(this);
    }

//    default <D, E> Function3<A, B, D, E> then(final Function2<? super C, ? super D, ? extends E> callable) {
//        return new Function3<A, B, D, E>() {
//            @Override
//            public E call(A a, B b, D d) throws Exception {
//                return callable.call(Function2.this.call(a, b), d);
//            }
//        };
//    }
}
