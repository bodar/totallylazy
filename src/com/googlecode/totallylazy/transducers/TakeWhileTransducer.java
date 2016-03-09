package com.googlecode.totallylazy.transducers;

import com.googlecode.totallylazy.predicates.Predicate;

import java.util.concurrent.atomic.AtomicBoolean;

import static com.googlecode.totallylazy.transducers.State.Stop;

public interface TakeWhileTransducer<A> extends Transducer<A, A> {
    Predicate<? super A> predicate();

    static <A> TakeWhileTransducer<A> takeWhileTransducer(Predicate<? super A> predicate) {
        AtomicBoolean complete = new AtomicBoolean(false);
        return new TakeWhileTransducer<A>() {
            @Override
            public Predicate<? super A> predicate() {
                return predicate;
            }

            @Override
            public Receiver<A> apply(Receiver<A> receiver) {
                return Receiver.receiver(receiver, item -> {
                    if (complete.get()) return Stop;
                    if (predicate.matches(item)) return receiver.next(item);
                    else {
                        complete.set(true);
                        receiver.finish();
                        return Stop;
                    }
                });
            }
        };
    }
}
