package com.googlecode.totallylazy.transducers;

import com.googlecode.totallylazy.Option;

import java.util.concurrent.atomic.AtomicBoolean;

import static com.googlecode.totallylazy.transducers.State.Stop;

public interface FirstOptionTransducer<T> extends Transducer<T, Option<T>> {
    static <T> FirstOptionTransducer<T> firstOptionTransducer() {
        AtomicBoolean complete = new AtomicBoolean(false);
        return receiver -> Receiver.receiver(receiver, item -> {
            receiver.next(Option.some(item));
            receiver.finish();
            complete.set(true);
            return Stop;
        }, () -> {
            if(!complete.get()) {
                receiver.next(Option.none());
                receiver.finish();
            }
        });
    }
}
