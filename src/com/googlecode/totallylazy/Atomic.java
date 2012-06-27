package com.googlecode.totallylazy;

import java.util.concurrent.atomic.AtomicReference;

public interface Atomic<T> extends Value<T> {
    Atomic<T> modify(Callable1<? super T, ? extends T> callable);

    class constructors {
        static <T> Atomic<T> atomic(final T t) {
            return new Atomic<T>() {
                private final AtomicReference<T> reference = new AtomicReference<T>(t);

                @Override
                public Atomic<T> modify(Callable1<? super T, ? extends T> callable) {
                    while (true){
                        T current = reference.get();
                        T modified = Callers.call(callable, current);
                        if(reference.compareAndSet(current, modified)) return this;
                    }
                }

                @Override
                public T value() {
                    return reference.get();
                }
            };
        }
    }
}
