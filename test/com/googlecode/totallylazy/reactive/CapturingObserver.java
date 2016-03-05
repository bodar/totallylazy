package com.googlecode.totallylazy.reactive;

import com.googlecode.totallylazy.Sequence;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static com.googlecode.totallylazy.Sequences.sequence;

public class CapturingObserver<T> implements Observer<T> {
    private final List<T> items = new CopyOnWriteArrayList<>();
    private final AtomicBoolean started = new AtomicBoolean(false);
    private final AtomicBoolean finished = new AtomicBoolean(false);
    private final AtomicReference<Throwable> error = new AtomicReference<>();

    public Sequence<T> items() {
        return sequence(items);
    }

    public boolean started() {
        return started.get();
    }

    public Throwable error() {
        return error.get();
    }

    public boolean finished() {
        return finished.get();
    }

    @Override
    public State start() {
        started.set(true);
        return State.Continue;
    }

    @Override
    public State next(T item) {
        items.add(item);
        return State.Continue;
    }

    @Override
    public void error(Throwable throwable) {
        error.set(throwable);
    }

    @Override
    public void finish() {
        finished.set(true);
    }
}
