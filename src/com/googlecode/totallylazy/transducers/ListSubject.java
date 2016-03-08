package com.googlecode.totallylazy.transducers;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.googlecode.totallylazy.transducers.State.Continue;
import static com.googlecode.totallylazy.transducers.State.Stop;

public class ListSubject<T> implements Subject<T> {
    protected final List<Receiver<T>> receivers = new CopyOnWriteArrayList<>();

    @Override
    public AutoCloseable send(Receiver<T> receiver) {
        receivers.add(receiver);
        return EMPTY_CLOSEABLE;
    }

    @Override
    public State start() {
        for (Receiver<T> receiver : receivers) {
            if (receiver.start().equals(Stop)) receivers.remove(receiver);
        }
        return receivers.isEmpty() ? Stop : Continue;
    }

    @Override
    public State next(T item) {
        for (Receiver<T> receiver : receivers) {
            if (receiver.next(item).equals(Stop)) receivers.remove(receiver);
        }
        return receivers.isEmpty() ? Stop : Continue;
    }

    @Override
    public void error(Throwable throwable) {
        for (Receiver<T> receiver : receivers) {
            receiver.error(throwable);
        }
    }

    @Override
    public void finish() {
        for (Receiver<T> receiver : receivers) {
            receiver.finish();
        }
    }
}
