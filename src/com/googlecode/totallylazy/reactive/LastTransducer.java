package com.googlecode.totallylazy.reactive;

import java.util.concurrent.atomic.AtomicReference;

import static com.googlecode.totallylazy.reactive.State.Continue;

public interface LastTransducer<T> extends Transducer<T, T> {
    static <T> LastTransducer<T> lastTransducer() {
        AtomicReference<T> reference = new AtomicReference<>();
        return observer -> Observer.observer(observer, item -> {
            reference.set(item);
            return Continue;
        }, () -> {
            T t = reference.get();
            if (t != null) {
                observer.next(t);
                observer.finish();
            }
        });
    }
}
