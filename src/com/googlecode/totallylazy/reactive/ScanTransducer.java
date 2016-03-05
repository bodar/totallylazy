package com.googlecode.totallylazy.reactive;

import com.googlecode.totallylazy.Function2;
import com.googlecode.totallylazy.Reducer;

import java.util.concurrent.atomic.AtomicReference;

public interface ScanTransducer<A, B> extends Transducer<A, B> {
    Function2<? super B, ? super A, ? extends B> reducer();

    static <A, B> ScanTransducer<A, B> scanTransducer(B seed, Function2<? super B, ? super A, ? extends B> reducer) {
        final AtomicReference<B> reference = new AtomicReference<>(seed);
        return new ScanTransducer<A, B>() {
            @Override
            public Observer<A> apply(Observer<B> observer) {
                return Observer.observer(observer, item ->
                        observer.next(reference.updateAndGet(s -> reducer().apply(s, item))));
            }

            @Override
            public Function2<? super B, ? super A, ? extends B> reducer() {
                return reducer;
            }
        };
    }

    static <A, B> ScanTransducer<A, B> scanTransducer(Reducer<B, ? super A> reducer) {
        return scanTransducer(reducer.identityElement(), reducer);
    }
}