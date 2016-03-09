package com.googlecode.totallylazy.transducers;

import java.util.concurrent.atomic.AtomicReference;

import static com.googlecode.totallylazy.transducers.State.Continue;

public interface LastTransducer<T> extends Transducer<T, T> {
    static <T> LastTransducer<T> lastTransducer() {
        AtomicReference<T> reference = new AtomicReference<>();
        return receiver -> Receiver.receiver(receiver, item -> {
            reference.set(item);
            return Continue;
        }, () -> {
            T t = reference.get();
            if (t != null) {
                receiver.next(t);
                receiver.finish();
            }
        });
    }
}
