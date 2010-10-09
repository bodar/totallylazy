package com.googlecode.totallylazy.iterators;

import com.googlecode.totallylazy.Option;

import java.util.Iterator;
import java.util.List;

import static com.googlecode.totallylazy.Option.none;
import static com.googlecode.totallylazy.Option.some;

public class MemorisedIterator<T> extends StatefulIterator<T> {
    private final Iterator<T> iterator;
    private final List<T> memory;
    private int position = 0;

    public MemorisedIterator(Iterator<T> iterator, List<T> memory) {
        this.iterator = iterator;
        this.memory = memory;
    }

    public Option<T> getNext() {
        synchronized (memory) {
            int currentPosition = position++;
            if (haveCachedAnswer(currentPosition)) {
                return some(getCachedAnswer(currentPosition));
            }

            if (iterator.hasNext()) {
                T t = iterator.next();
                memory.add(t);
                return some(t);
            }

            return none();
        }
    }

    private boolean haveCachedAnswer(int position) {
        return position < memory.size();
    }

    private T getCachedAnswer(int position) {
        return memory.get(position);
    }
}
