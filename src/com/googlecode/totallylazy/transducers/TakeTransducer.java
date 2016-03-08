package com.googlecode.totallylazy.transducers;

import java.util.concurrent.atomic.AtomicInteger;

import static com.googlecode.totallylazy.transducers.State.Continue;
import static com.googlecode.totallylazy.transducers.State.Stop;

public interface TakeTransducer<A> extends Transducer<A, A> {
    int limit();

    static <A> TakeTransducer<A> takeTransducer(int limit) {
        AtomicInteger count = new AtomicInteger();
        return new TakeTransducer<A>() {
            @Override
            public int limit() {
                return limit;
            }

            @Override
            public Receiver<A> apply(Receiver<A> receiver) {
                return Receiver.receiver(receiver, () -> {
                    receiver.start();
                    if (limit == 0) {
                        receiver.finish();
                        return Stop;
                    } else return Continue;
                }, item -> {
                    int position = count.incrementAndGet();
                    if (position == limit) {
                        receiver.next(item);
                        receiver.finish();
                        return Stop;
                    }
                    if (position > limit) return Stop;
                    return receiver.next(item);
                });
            }
        };
    }
}
