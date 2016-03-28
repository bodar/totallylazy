package com.googlecode.totallylazy.transducers;

import static com.googlecode.totallylazy.transducers.State.Stop;

public interface FirstTransducer<T> extends Transducer<T, T> {
    static <T> FirstTransducer<T> firstTransducer() {
        return receiver -> Receiver.receiver(receiver, item -> {
            receiver.next(item);
            receiver.finish();
            return Stop;
        });
    }
}
