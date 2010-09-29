package com.googlecode.totallylazy.iterators;

import java.util.Iterator;
import java.util.List;

public class MemoriseIterator<T> extends ReadOnlyIterator<T> {
    private final Iterator<T> iterator;
    private final List<T> memory;
    private int position = 0;

    public MemoriseIterator(Iterator<T> iterator, List<T> memory) {
        this.iterator = iterator;
        this.memory = memory;
    }

    public boolean hasNext() {
        synchronized (memory) {
            return haveCachedAnswer(position) || iterator.hasNext();
        }
    }

    public T next() {
        synchronized (memory) {
            int currentPosition = position++;
            if (haveCachedAnswer(currentPosition)) {
                return getCachedAnswer(currentPosition);
            }

            T t = iterator.next();
            memory.add(t);
            return t;
        }
    }

    private boolean haveCachedAnswer(int position) {
        return position < memory.size();
    }

    private T getCachedAnswer(int position) {
        return memory.get(position);
    }
}
