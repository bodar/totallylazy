package com.googlecode.totallylazy;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.Method;

import static com.googlecode.totallylazy.Callers.call;
import static com.googlecode.totallylazy.Runnables.VOID;

public class Closeables {
    public static <T extends Closeable> Callable1<? super T, Void> closeAfter(final Callable1<? super T, Void> callable) {
        return new Callable1<T, Void>() {
            public Void call(T t) throws Exception {
                return using(t, callable);
            }
        };
    }

    public static <T extends Closeable> T close(final T t) {
        try {
            t.close();
        } catch (IOException e) {
            throw new LazyException(e);
        }
        return t;
    }

    public static <T extends Closeable, R> R using(T t, Callable1<? super T, R> callable) {
        try {
            return call(callable, t);
        } finally {
            call(close(), t);
        }
    }

    public static <T, R> R using(T instanceWithCloseMethod, Callable1<? super T, R> callable) {
        try {
            return call(callable, instanceWithCloseMethod);
        } finally {
            call(reflectiveClose(), instanceWithCloseMethod);
        }
    }

    public static <T> Callable1<T, Void> reflectiveClose() {
        return new Callable1<T, Void>() {
            public Void call(T instanceWithCloseMethod) throws Exception {
                final Option<Method> close = Methods.method(instanceWithCloseMethod, "close");
                if (close.isEmpty()) {
                    throw new IllegalArgumentException("Instance T must have a 'close()' method");
                }

                Methods.invoke(close.get(), instanceWithCloseMethod);
                return VOID;
            }
        };
    }

    public static Callable1<Closeable, Void> close() {
        return new Callable1<Closeable, Void>() {
            public Void call(Closeable closeable) throws IOException {
                closeable.close();
                return VOID;
            }
        };
    }

}
