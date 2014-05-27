package com.googlecode.totallylazy;

import com.googlecode.totallylazy.annotations.multimethod;
import com.googlecode.totallylazy.predicates.LogicalPredicate;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import static com.googlecode.totallylazy.Closeables.using;
import static com.googlecode.totallylazy.FileSource.fileSource;
import static com.googlecode.totallylazy.LazyException.lazyException;
import static com.googlecode.totallylazy.ZipSource.zipSource;

public interface Sources extends Closeable {
    Sequence<Source> sources();

    class constructors {
        public static Sources sources(Uri uri) {
            if (uri.scheme().equals(Uri.FILE_SCHEME)) return fileSource(uri.toFile());
            if (uri.scheme().equals(Uri.JAR_SCHEME)) {
                return zipSource(inputStream(Uri.uri(uri.authority())), uri.path());
            }
            throw new UnsupportedOperationException();
        }

        private static InputStream inputStream(Uri locationJar) {
            try {
                return locationJar.toURL().openStream();
            } catch (IOException e) {
                throw lazyException(e);
            }
        }
    }

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
            return sources.sources().each((Source source) -> {
                Streams.copyAndClose(source.input, destination.destination(source.name, source.modified));
            }).size();
        }
    }

    class functions {
        public static Function<Source, String> name = new Function<Source, String>() {
            @Override
            public String call(Source source) throws Exception {
                return source.name;
            }
        };
        public static LogicalPredicate<Source> directory = new LogicalPredicate<Source>() {
            @Override
            public boolean matches(Source source) {
                return source.isDirectory;
            }
        };
    }

    class Source extends Eq {
        public final String name;
        public final Date modified;
        public final InputStream input;
        public final boolean isDirectory;

        public Source(String name, Date modified, InputStream input, boolean isDirectory) {
            this.name = name;
            this.modified = modified;
            this.input = input;
            this.isDirectory = isDirectory;
        }

        @multimethod
        public boolean equals(Source other) {
            return name.equals(other.name) && modified.equals(other.modified) && isDirectory == other.isDirectory;
        }

        @Override
        public int hashCode() {
            return name.hashCode() * modified.hashCode() * Boolean.valueOf(isDirectory).hashCode() * 31;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
