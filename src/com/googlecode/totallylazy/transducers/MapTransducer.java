package com.googlecode.totallylazy.transducers;

import com.googlecode.totallylazy.functions.Function1;

import static com.googlecode.totallylazy.transducers.State.Stop;

public interface MapTransducer<A, B> extends Transducer<A, B> {
    Function1<? super A, ? extends B> mapper();

    static <A, B> MapTransducer<A, B> mapTransducer(Function1<? super A, ? extends B> mapper) {
        return () -> mapper;
    }

    @Override
    default Receiver<A> apply(Receiver<B> receiver) {
        return Receiver.receiver(receiver,
                item -> {
                    try {
                        return receiver.next(mapper().call(item));
                    } catch (Exception e) {
                        receiver.error(e);
                        return Stop;
                    }
                });
    }
}
