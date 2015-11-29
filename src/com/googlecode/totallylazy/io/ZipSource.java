package com.googlecode.totallylazy.io;

import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Unchecked;
import com.googlecode.totallylazy.functions.Unary;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.zip.ZipInputStream;

import static com.googlecode.totallylazy.io.FilterSource.filterSource;
import static com.googlecode.totallylazy.predicates.Predicates.*;

public class ZipSource implements Sources {
    final ZipInputStream in;

    private ZipSource(InputStream inputStream) {
        in = inputStream instanceof ZipInputStream ? Unchecked.<ZipInputStream>cast(inputStream) : new ZipInputStream(inputStream);
    }

    public static ZipSource zipSource(InputStream inputStream) {
        return new ZipSource(inputStream);
    }

    public static Sources zipSource(InputStream inputStream, String rawFolder) {
        String folder = rawFolder.replaceFirst("/", "");
        return filterSource(where(Source::name, is(not(""))),
                MapSources.mapSource(removeFolderFromName(folder),
                        filterSource(source -> source.name().startsWith(folder), zipSource(inputStream))));
    }

    private static Unary<Source> removeFolderFromName(final String folder) {
        return source -> Source.source(source.name().replaceFirst("^" + folder, ""), source::modified, source::input, source.isDirectory());
    }

    @Override
    public Sequence<Source> sources() {
        return Zip.entries(in).map(zipEntry -> Source.source(zipEntry.getName(), () -> new Date(zipEntry.getTime()), IgnoreCloseInputStream::new, zipEntry.isDirectory()));
    }

    @Override
    public void close() throws IOException {
        in.close();
    }

    private class IgnoreCloseInputStream extends InputStream {
        @Override
        public int read() throws IOException {
            return in.read();
        }

        @Override
        public int read(byte[] b) throws IOException {
            return in.read(b);
        }

        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            return in.read(b, off, len);
        }
    }
}
