package com.googlecode.totallylazy.reactive;

import com.googlecode.totallylazy.Sequence;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static com.googlecode.totallylazy.Sequences.sequence;

public class CapturingObserver<T> implements Observer<T> {
    private final List<T> items = new CopyOnWriteArrayList<>();
    private final AtomicBoolean completed = new AtomicBoolean(false);
    private final AtomicReference<Throwable> error = new AtomicReference<>();

    public Sequence<T> items() {
        return sequence(items);
    }

    public boolean completed() {
        return completed.get();
    }

    public Throwable error() {
        return error.get();
    }

    @Override
    public void step(State<T> state) {
        if (state instanceof Next) items.add(state.value());
        if (state instanceof Error) {
            try {
                state.value();
            } catch (Exception e) {
                error.set(e);
            }
        }
        ;
        if (state instanceof Complete) completed.set(true);
    }
}
