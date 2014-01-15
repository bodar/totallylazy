package com.googlecode.totallylazy;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.Method;

import static com.googlecode.totallylazy.Sequences.sequence;

public class Closeables {
    public static <T extends Closeable, R> Function<T, R> closeAfter(final Function<? super T, R> callable) {
        return new Function<T, R>() {
            @Override
            public R call(T t) throws Exception {
                return using(t, callable);
            }
        };
    }

    public static <T> T safeClose(final T t) {
        try {
            close(t);
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    @SafeVarargs
    public static <T> void safeClose(final T... t) {
        reflectiveSafeClose(sequence(t));
    }

    public static <T> void reflectiveSafeClose(final Iterable<? extends T> t) {
        sequence(t).each(reflectiveSafeClose());
    }

    public static <T extends Closeable> T safeClose(final T t) {
        try {
            close(t);
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    @SafeVarargs
    public static <T extends Closeable> void safeClose(final T... t) {
        safeClose(sequence(t));
    }

    public static <T extends Closeable> void safeClose(final Iterable<? extends T> t) {
        sequence(t).each(safeClose());
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

    public static <T extends Closeable, R> R using(T t, Function<? super T, ? extends R> callable) {
        try {
            return callable.apply(t);
        } finally {
            close().apply(t);
        }
    }

    public static <A extends Closeable, B extends Closeable, R> R using(A a, B b, BiFunction<? super A, ? super B, ? extends R> callable) {
        try {
            return Callers.call(callable, a, b);
        } finally {
            try {
                close().apply(a);
            } finally {
                close().apply(b);
            }
        }
    }

    public static <T, R> R using(T instanceWithCloseMethod, Function<? super T, ? extends R> callable) {
        try {
            return callable.apply(instanceWithCloseMethod);
        } finally {
            reflectiveClose().apply(instanceWithCloseMethod);
        }
    }

    public static <T> Block<T> reflectiveClose() {
        return new Block<T>() {
            @Override
            protected void execute(T instanceWithCloseMethod) throws Exception {
                close(instanceWithCloseMethod);
            }
        };
    }

    public static Block<Closeable> close() {
        return new Block<Closeable>() {
            @Override
            protected void execute(Closeable closeable) throws Exception {
                close(closeable);
            }
        };
    }

    public static Block<Closeable> safeClose() {
        return new Block<Closeable>() {
            @Override
            protected void execute(Closeable closeable) throws Exception {
                safeClose(closeable);
            }
        };
    }

    public static <T> Block<T> reflectiveSafeClose() {
        return new Block<T>() {
            @Override
            protected void execute(T closeable) throws Exception {
                safeClose(closeable);
            }
        };
    }
}