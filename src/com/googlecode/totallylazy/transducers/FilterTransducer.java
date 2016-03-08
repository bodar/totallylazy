package com.googlecode.totallylazy.transducers;


import com.googlecode.totallylazy.predicates.Predicate;

import static com.googlecode.totallylazy.transducers.State.Continue;

public interface FilterTransducer<A> extends Transducer<A, A> {
    Predicate<? super A> predicate();

    static <A> FilterTransducer<A> filterTransducer(Predicate<? super A> predicate) { return () -> predicate;}

    @Override
    default Receiver<A> apply(Receiver<A> receiver) {
        return Receiver.receiver(receiver, item -> {
            if (!predicate().matches(item)) return Continue;
            return receiver.next(item);
        });
    }
}