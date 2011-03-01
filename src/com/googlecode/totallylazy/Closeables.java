package com.googlecode.totallylazy;

import java.io.Closeable;
import java.io.IOException;

public class Closeables {
    public static <T extends Closeable> Runnable1<T> closeAfter(final Runnable1<T> decorated) {
        return new Runnable1<T>() {
            public void run(T closeable) {
                try {
                    decorated.run(closeable);
                } finally {
                    close(closeable);
                }
            }
        };
    }

    public static <T extends Closeable> T close(T closeable) {
        try {
            closeable.close();
        } catch (IOException e) {
            throw new LazyException("Could not close Closeable", e);
        }
        return closeable;
    }
}
