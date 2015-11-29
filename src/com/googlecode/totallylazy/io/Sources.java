package com.googlecode.totallylazy.io;

import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Streams;
import com.googlecode.totallylazy.functions.Function1;
import com.googlecode.totallylazy.predicates.LogicalPredicate;
import com.googlecode.totallylazy.predicates.Predicate;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

import static com.googlecode.totallylazy.functions.Block.block;
import static com.googlecode.totallylazy.Closeables.using;
import static com.googlecode.totallylazy.io.FileSource.fileSource;
import static com.googlecode.totallylazy.LazyException.lazyException;
import static com.googlecode.totallylazy.io.ZipSource.zipSource;

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
            return using(source, destination, methods::copy);
        }

        public static int copy(Sources sources, final Destination destination) {
            return sources.sources().map(block(source ->
                    Streams.copyAndClose(source.input(), destination.destination(source.name(), source.modified())))).size();
        }
    }

}
