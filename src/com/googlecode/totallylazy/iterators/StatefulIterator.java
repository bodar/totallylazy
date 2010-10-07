package com.googlecode.totallylazy.iterators;

import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Peekable;

import java.util.NoSuchElementException;

import static com.googlecode.totallylazy.Option.none;

public abstract class StatefulIterator<T> extends ReadOnlyIterator<T> implements Peekable<T> {
    private Option<T> state = none();
    private boolean finished = false;

    protected abstract Option<T> getNext();

    public final boolean hasNext() {
        if(finished){
            return false;
        }

        if (state.isEmpty()) {
            state = getNext();
            if (state.isEmpty()) {
                finished = true;
                return false;
            }
        }
        return true;
    }

    public final T next() {
        if (hasNext()) {
            return pop(state);
        }
        throw new NoSuchElementException();
    }

    public final T peek() {
        if (hasNext()) {
            return state.get();
        }
        throw new NoSuchElementException();
    }

    private T pop(Option<T> value) {
        state = none();
        return value.get();
    }
}
