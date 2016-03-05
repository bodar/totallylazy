package com.googlecode.totallylazy.reactive;

import java.util.concurrent.atomic.AtomicInteger;

import static com.googlecode.totallylazy.reactive.State.Continue;
import static com.googlecode.totallylazy.reactive.State.Stop;

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
            public Observer<A> apply(Observer<A> observer) {
                return Observer.observer(observer, () -> {
                    observer.start();
                    if (limit == 0) {
                        observer.finish();
                        return Stop;
                    } else return Continue;
                }, item -> {
                    int position = count.incrementAndGet();
                    if (position == limit) {
                        observer.next(item);
                        observer.finish();
                        return Stop;
                    }
                    if (position > limit) return Stop;
                    return observer.next(item);
                });
            }
        };
    }
}
