package com.googlecode.totallylazy;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.Method;

import static com.googlecode.totallylazy.Callers.call;
import static com.googlecode.totallylazy.Runnables.VOID;

public class Closeables {
    public static <T extends Closeable> Function1<T, Void> closeAfter(final Callable1<? super T, Void> callable) {
        return new Function1<T, Void>() {
            public Void call(T t) throws Exception {
                return using(t, callable);
            }
        };
    }

    public static <T> T safeClose(final T t){
        try {
            close(t);
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public static <T extends Closeable> T safeClose(final T t){
        try {
            close(t);
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> T close(final T t) {
        if (t == null) {
            return t;
        }

        final Option<Method> close = Methods.method(t, "close");
        if (close.isEmpty()) {
            return t;
        }

        Methods.invoke(close.get(), t);
        return t;
    }


    public static <T extends Closeable> T close(final T t) {
        if (t == null) {
            return t;
        }
        try {
            t.close();
        } catch (IOException e) {
            throw LazyException.lazyException(e);
        }
        return t;
    }

    public static <T extends Closeable, R> R using(T t, Callable1<? super T, ? extends R> callable) {
        try {
            return call(callable, t);
        } finally {
            call(close(), t);
        }
    }

    public static <T, R> R using(T instanceWithCloseMethod, Callable1<? super T, ? extends R> callable) {
        try {
            return call(callable, instanceWithCloseMethod);
        } finally {
            call(reflectiveClose(), instanceWithCloseMethod);
        }
    }

    public static <T> Function1<T, Void> reflectiveClose() {
        return new Function1<T, Void>() {
            public Void call(T instanceWithCloseMethod) throws Exception {
                close(instanceWithCloseMethod);
                return VOID;
            }
        };
    }

    public static Function1<Closeable, Void> close() {
        return new Function1<Closeable, Void>() {
            public Void call(Closeable closeable) throws IOException {
                close(closeable);
                return VOID;
            }
        };
    }

    public static Function1<Closeable, Void> safeClose() {
        return new Function1<Closeable, Void>() {
            public Void call(Closeable closeable) throws IOException {
                safeClose(closeable);
                return VOID;
            }
        };
    }
}
