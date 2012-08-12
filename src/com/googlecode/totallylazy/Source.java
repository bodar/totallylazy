package com.googlecode.totallylazy;

import java.io.Closeable;
import java.io.InputStream;

import static com.googlecode.totallylazy.Closeables.using;
import static com.googlecode.totallylazy.Streams.copyAndClose;

public interface Source extends Closeable {
    Sequence<Pair<String, InputStream>> sources();

    class methods {
        public static Integer copy(Source source, final Destination destination) {
            return using(source, new Function1<Source, Integer>() {
                @Override
                public Integer call(final Source source) throws Exception {
                    return using(destination, new Callable1<Destination, Integer>() {
                        @Override
                        public Integer call(final Destination destination) throws Exception {
                            return source.sources().map(new Function1<Pair<String, InputStream>, Void>() {
                                @Override
                                public Void call(Pair<String, InputStream> pair) throws Exception {
                                    return copyAndClose(pair.second(), destination.destination(pair.first()));
                                }
                            }).size();
                        }
                    });
                }
            });
        }
    }
}
