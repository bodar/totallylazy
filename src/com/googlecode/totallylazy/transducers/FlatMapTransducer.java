package com.googlecode.totallylazy.transducers;


import com.googlecode.totallylazy.functions.Function1;

import java.util.concurrent.atomic.AtomicBoolean;

import static com.googlecode.totallylazy.transducers.State.Continue;
import static com.googlecode.totallylazy.transducers.State.Stop;

public interface FlatMapTransducer<A, B> extends Transducer<A, B> {
    Function1<? super A, ? extends Sender<B>> mapper();

    static <A, B> FlatMapTransducer<A, B> flatMapTransducer(Function1<? super A, ? extends Sender<B>> mapper) {
        AtomicBoolean complete = new AtomicBoolean(false);
        return new FlatMapTransducer<A, B>() {
            @Override
            public Function1<? super A, ? extends Sender<B>> mapper() {
                return mapper;
            }

            @Override
            public Receiver<A> apply(Receiver<B> receiver) {
                return Receiver.receiver(receiver, a -> {
                    mapper.apply(a).send(Receiver.receiver(receiver, b -> {
                        State state = receiver.next(b);
                        if (state.equals(Stop)) complete.set(true);
                        return state;
                    }));
                    return complete.get() ? Stop : Continue;
                });
            }
        };
    }
}