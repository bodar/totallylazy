package com.googlecode.totallylazy.iterators;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public final class MemorisedIterator<T> extends ReadOnlyListIterator<T> {
    private final Iterator<T> iterator;
    private final List<T> memory;
    private volatile int position = 0;

    public MemorisedIterator(final Iterator<T> iterator, final List<T> memory) {
        this.iterator = iterator;
        this.memory = memory;
    }

    public int nextIndex() {
        return position;
    }

    public boolean hasNext() {
        synchronized (memory) {
            return hasCachedAnswer() || iterator.hasNext();
        }
    }

    public T next() {
        synchronized (memory) {
            if (hasNext()) {
                if (hasCachedAnswer()) {
                    return cachedAnswer(position++);
                }

                if (iterator.hasNext()) {
                    T t = iterator.next();
                    memory.add(t);
                    position++;
                    return t;
                }
            }
            throw new NoSuchElementException();
        }
    }

    public int previousIndex() {
        return position - 1;
    }

    public boolean hasPrevious() {
        return position > 0;
    }

    public T previous() {
        synchronized (memory) {
            if (hasPrevious()) {
                return cachedAnswer(--position);
            }
            throw new NoSuchElementException();
        }
    }

    private boolean hasCachedAnswer() {
        synchronized (memory) {
            return position < memory.size();
        }
    }

    private T cachedAnswer(final int position) {
        synchronized (memory) {
            return memory.get(position);
        }
    }
}
