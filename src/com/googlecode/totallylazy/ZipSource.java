package com.googlecode.totallylazy;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static com.googlecode.totallylazy.FilterSource.filterSource;
import static com.googlecode.totallylazy.MapSources.mapSource;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Predicates.not;
import static com.googlecode.totallylazy.Predicates.where;
import static com.googlecode.totallylazy.Sources.functions.name;
import static com.googlecode.totallylazy.Strings.startsWith;

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
        return filterSource(where(name, is(not(""))),
                mapSource(removeFolderFromName(folder),
                        filterSource(where(name, startsWith(folder)), zipSource(inputStream))));
    }

    private static UnaryFunction<Source> removeFolderFromName(final String folder) {
        return new UnaryFunction<Source>() {
            @Override
            public Source call(Source source) throws Exception {
                return new Source(source.name.replaceFirst("^" + folder, ""), source.modified, source.input, source.isDirectory);
            }
        };
    }

    @Override
    public Sequence<Source> sources() {
        return Zip.entries(in).map(new Function1<ZipEntry, Source>() {
            @Override
            public Source call(ZipEntry zipEntry) throws Exception {
                return new Source(zipEntry.getName(), new Date(zipEntry.getTime()), new IgnoreCloseInputStream(), zipEntry.isDirectory());
            }
        });
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
