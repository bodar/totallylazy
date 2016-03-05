package com.googlecode.totallylazy.reactive;

import com.googlecode.totallylazy.Function;

import java.util.concurrent.atomic.AtomicBoolean;

import static com.googlecode.totallylazy.reactive.State.Continue;
import static com.googlecode.totallylazy.reactive.State.Stop;

public interface FlatMapTransducer<A, B> extends Transducer<A, B> {
    Function<? super A, ? extends Observable<B>> mapper();

    static <A, B> FlatMapTransducer<A, B> flatMapTransducer(Function<? super A, ? extends Observable<B>> mapper) {
        AtomicBoolean complete = new AtomicBoolean(false);
        return new FlatMapTransducer<A, B>() {
            @Override
            public Function<? super A, ? extends Observable<B>> mapper() {
                return mapper;
            }

            @Override
            public Observer<A> apply(Observer<B> observer) {
                return Observer.observer(observer, a -> {
                    mapper.apply(a).subscribe(Observer.observer(observer, b -> {
                        State state = observer.next(b);
                        if (state.equals(Stop)) complete.set(true);
                        return state;
                    }));
                    return complete.get() ? Stop : Continue;
                });
            }
        };
    }
}