package com.googlecode.totallylazy.transducers;

import java.util.concurrent.atomic.AtomicInteger;

import static com.googlecode.totallylazy.transducers.State.Continue;

public interface DropTransducer<A> extends Transducer<A, A> {
    int limit();

    static <A> Transducer<A, A> dropTransducer(int limit) {
        if (limit == 0) return Transducers.identity();
        AtomicInteger count = new AtomicInteger();
        return new DropTransducer<A>() {
            @Override
            public int limit() {
                return limit;
            }

            @Override
            public Receiver<A> apply(Receiver<A> receiver) {
                return Receiver.receiver(receiver, item -> {
                    int position = count.getAndIncrement();
                    if (position < limit) {
                        return Continue;
                    }
                    return receiver.next(item);
                });
            }
        };
    }
}
