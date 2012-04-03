package com.googlecode.totallylazy;

import com.googlecode.totallylazy.iterators.EmptyIterator;
import com.googlecode.totallylazy.iterators.StatefulIterator;

import java.util.Iterator;

import static com.googlecode.totallylazy.Predicates.in;
import static com.googlecode.totallylazy.Predicates.not;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Sets.set;

public abstract class IList<T> implements Iterable<T> {
    public IList<T> cons(T value) {
        return new Cons<T>(value, this);
    }

    public IList<T> removeAll(Iterable<T> values) {
        return sequence(this).filter(not(in(set(values)))).fold(new Empty<T>(), new Callable2<IList<T>, T, IList<T>>() {
            @Override
            public IList<T> call(IList<T> ts, T t) throws Exception {
                return ts.cons(t);
            }
        });
    }

    public abstract int size();

    public static <T> IList<T> empty() {
        return new Empty<T>();
    }

    public static <T> IList<T> list(T value) {
        return IList.<T>empty().cons(value);
    }

    static class Empty<T> extends IList<T> {
        @Override
        public int size() {
            return 0;
        }

        @Override
        public Iterator<T> iterator() {
            return new EmptyIterator<T>();
        }
    }

    static class Cons<T> extends IList<T> {
        private final T head;
        private final IList<T> tail;

        Cons(T head, IList<T> tail) {
            this.head = head;
            this.tail = tail;
        }


        @Override
        public int size() {
            return 1 + tail.size();
        }

        @Override
        public Iterator<T> iterator() {
            return Iterators.flatten(sequence(new OneElementIterator<T>(head), tail.iterator()).iterator());
        }

        private class OneElementIterator<T> extends StatefulIterator<T> {
            private final T value;
            private boolean called;

            public OneElementIterator(T value) {
                this.value = value;
            }

            @Override
            protected T getNext() throws Exception {
                if (called) {
                    return finished();
                }
                called = true;
                return value;
            }
        }
    }
}