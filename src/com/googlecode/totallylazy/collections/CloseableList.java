package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Closeables;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.googlecode.totallylazy.Sequences.sequence;

public interface CloseableList<T extends Closeable> extends List<T>, Closeable {
    T manage(T instance);

    public static class constructors {
        @SuppressWarnings("unchecked")
        public static <T extends Closeable> CloseableList<T> closeableList(Class<T> aClass) {
            return closeableList();
        }

        @SafeVarargs
        public static <T extends Closeable> CloseableList<T> closeableList(T... items) {
            return closeableList(new CopyOnWriteArrayList<T>(items));
        }

        public static <T extends Closeable> CloseableList<T> closeableList(List<? super T> items) {
            return new CloseableDelegatingList<T>(items);
        }

        private static class CloseableDelegatingList<T extends Closeable> extends DelegatingList<T> implements CloseableList<T> {
            public CloseableDelegatingList(List<? super T> items) {super(items);}

            public void close() throws IOException {
                sequence(delegate).each(Closeables.safeClose());
                delegate.clear();
            }

            public T manage(T instance) {
                add(instance);
                return instance;
            }
        }
    }

}
