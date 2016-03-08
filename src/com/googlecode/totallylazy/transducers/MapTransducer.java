package com.googlecode.totallylazy.transducers;

import com.googlecode.totallylazy.functions.Function1;

public interface MapTransducer<A, B> extends Transducer<A, B> {
    Function1<? super A, ? extends B> mapper();

    static <A, B> MapTransducer<A, B> mapTransducer(Function1<? super A, ? extends B> mapper) {return () -> mapper;}

    @Override
    default Receiver<A> apply(Receiver<B> receiver) {
        return Receiver.receiver(receiver,
                item -> receiver.next(mapper().apply(item)));
    }
}
