package com.googlecode.totallylazy.iterators;

import java.util.Iterator;
import java.util.List;

public final class MemorisedIterator<T> extends StatefulIterator<T> {
    private final Iterator<T> iterator;
    private final List<T> memory;
    private int position = 0;

    public MemorisedIterator(final Iterator<T> iterator, final List<T> memory) {
        this.iterator = iterator;
        this.memory = memory;
    }

    public final T getNext() {
        synchronized (memory) {
            int currentPosition = position++;
            if (haveCachedAnswer(currentPosition)) {
                return getCachedAnswer(currentPosition);
            }

            if (iterator.hasNext()) {
                T t = iterator.next();
                memory.add(t);
                return t;
            }

            return finished();
        }
    }

    private boolean haveCachedAnswer(int position) {
        return position < memory.size();
    }

    private T getCachedAnswer(int position) {
        return memory.get(position);
    }
}
