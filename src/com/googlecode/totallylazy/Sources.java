package com.googlecode.totallylazy;

import com.googlecode.totallylazy.annotations.multimethod;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import static com.googlecode.totallylazy.Closeables.using;
import static com.googlecode.totallylazy.FileSource.fileSource;
import static com.googlecode.totallylazy.FilterSource.filterSource;
import static com.googlecode.totallylazy.LazyException.lazyException;
import static com.googlecode.totallylazy.Predicates.where;
import static com.googlecode.totallylazy.Sources.functions.name;
import static com.googlecode.totallylazy.Strings.startsWith;
import static com.googlecode.totallylazy.ZipSource.zipSource;

public interface Sources extends Closeable {
    Sequence<Source> sources();

    class constructors {
        public static Sources sources(Uri uri){
            if(uri.scheme().equals(Uri.FILE_SCHEME)) return fileSource(uri.toFile());
            if(uri.scheme().equals(Uri.JAR_SCHEME)) {
                try {
                    Uri locationJar = Uri.uri(uri.authority());
                    return filterSource(where(name, startsWith(uri.path().replaceFirst("/", ""))), zipSource(locationJar.toURL().openStream()));
                } catch (IOException e) {
                    throw lazyException(e);
                }
            }
            throw new UnsupportedOperationException();
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
            return sources.sources().map(new Block<Source>() {
                @Override
                protected void execute(Source source) throws Exception {
                    Streams.copyAndClose(source.input, destination.destination(source.name, source.modified));
                }
            }).size();
        }
    }

    class functions{
        public static Mapper<Source, String> name = new Mapper<Source, String>() {
            @Override
            public String call(Source source) throws Exception {
                return source.name;
            }
        };
    }

    class Source extends Eq {
        public final String name;
        public final Date modified;
        public final InputStream input;

        public Source(String name, Date modified, InputStream input) {
            this.name = name;
            this.modified = modified;
            this.input = input;
        }

        @multimethod
        public boolean equals(Source other) {
            return name.equals(other.name) && modified.equals(other.modified);
        }

        @Override
        public int hashCode() {
            return name.hashCode() * modified.hashCode() * 31;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
