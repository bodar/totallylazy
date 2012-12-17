package com.googlecode.totallylazy;

import java.io.Closeable;
import java.io.InputStream;
import java.util.Date;

import static com.googlecode.totallylazy.Closeables.using;

public interface Sources extends Closeable {
    Sequence<Source> sources();

    class methods {
        public static int copyAndClose(Sources source, final Destination destination) {
            return using(source, destination, new Function2<Sources, Destination, Integer>() {
                @Override
                public Integer call(Sources source, Destination destination) throws Exception {
                    return copy(source, destination);
                }
            });
        }

        public static int copy(Sources sources, final Destination destination) {
            return sources.sources().map(new Block<Source>() {
                @Override
                protected void execute(Source source) throws Exception {
                    Streams.copyAndClose(source.input, destination.destination(source.name, source.modified));
                }
            }).size();
        }
    }

    class Source {
        public final String name;
        public final Date modified;
        public final InputStream input;

        public Source(String name, Date modified, InputStream input) {
            this.name = name;
            this.modified = modified;
            this.input = input;
        }
    }
}
