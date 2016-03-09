package com.googlecode.totallylazy.transducers;

import com.googlecode.totallylazy.predicates.Predicate;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static com.googlecode.totallylazy.transducers.State.Continue;

public interface DropWhileTransducer<A> extends Transducer<A, A> {
    Predicate<? super A> predicate();

    static <A> Transducer<A, A> dropWhileTransducer(Predicate<? super A> predicate) {
        AtomicBoolean complete = new AtomicBoolean(false);
        return new DropWhileTransducer<A>() {
            @Override
            public Predicate<? super A> predicate() {
                return predicate;
            }

            @Override
            public Receiver<A> apply(Receiver<A> receiver) {
                return Receiver.receiver(receiver, item -> {
                    if (!complete.get()) {
                        if (predicate.matches(item)) return Continue;
                        complete.set(true);
                    }
                    return receiver.next(item);
                });
            }
        };
    }
}
