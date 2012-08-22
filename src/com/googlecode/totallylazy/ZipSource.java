package com.googlecode.totallylazy;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipSource implements Source {
    final ZipInputStream in;

    private ZipSource(InputStream inputStream) {
        in = new ZipInputStream(inputStream);
    }

    public static ZipSource zipSource(InputStream inputStream) {
        return new ZipSource(inputStream);
    }

    @Override
    public Sequence<Pair<String, InputStream>> sources() {
        return Zip.entries(in).map(new Function1<ZipEntry, Pair<String, InputStream>>() {
            @Override
            public Pair<String, InputStream> call(ZipEntry zipEntry) throws Exception {
                return Pair.<String, InputStream>pair(zipEntry.getName(), new IgnoreCloseInputStream());
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
