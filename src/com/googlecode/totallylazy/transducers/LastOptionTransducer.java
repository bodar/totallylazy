package com.googlecode.totallylazy.transducers;

import com.googlecode.totallylazy.Option;

import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicReference;

import static com.googlecode.totallylazy.transducers.State.Continue;

public interface LastOptionTransducer<T> extends Transducer<T, Option<T>> {
    static <T> LastOptionTransducer<T> lastOptionTransducer() {
        AtomicReference<T> reference = new AtomicReference<>();
        return receiver -> Receiver.receiver(receiver, item -> {
            reference.set(item);
            return Continue;
        }, () -> {
            receiver.next(Option.option(reference.get()));
            receiver.finish();
        });
    }
}
