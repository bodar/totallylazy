package com.googlecode.totallylazy.transducers;

import com.googlecode.totallylazy.functions.Function2;
import com.googlecode.totallylazy.functions.Reducer;

import java.util.concurrent.atomic.AtomicReference;

public interface ScanTransducer<A, B> extends Transducer<A, B> {
    Function2<? super B, ? super A, ? extends B> reducer();

    static <A, B> ScanTransducer<A, B> scanTransducer(B seed, Function2<? super B, ? super A, ? extends B> reducer) {
        final AtomicReference<B> reference = new AtomicReference<>(seed);
        return new ScanTransducer<A, B>() {
            @Override
            public Receiver<A> apply(Receiver<B> receiver) {
                return Receiver.receiver(receiver, item ->
                        receiver.next(reference.updateAndGet(s -> reducer().apply(s, item))));
            }

            @Override
            public Function2<? super B, ? super A, ? extends B> reducer() {
                return reducer;
            }
        };
    }

    static <A, B> ScanTransducer<A, B> scanTransducer(Reducer<? super A, B> reducer) {
        return scanTransducer(reducer.identity(), reducer);
    }
}