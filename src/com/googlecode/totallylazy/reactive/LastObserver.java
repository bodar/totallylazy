package com.googlecode.totallylazy.reactive;

import java.util.concurrent.atomic.AtomicReference;

import static com.googlecode.totallylazy.reactive.Next.next;

public class LastObserver<T> implements Observer<T> {
    private final AtomicReference<T> reference = new AtomicReference<>();
    private final Observer<T> observer;

    public LastObserver(Observer<T> observer) {
        this.observer = observer;
    }

    @Override
    public void step(State<T> state) {
        if(state instanceof Next) {
            reference.set(state.value());
            return;
        }
        if(state instanceof Complete) {
            T t = reference.get();
            if( t != null) observer.step(next(t));
        }
        observer.step(state);
    }
}
