package com.googlecode.totallylazy;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipSource implements Sources {
    final ZipInputStream in;

    private ZipSource(InputStream inputStream) {
        in = inputStream instanceof ZipInputStream ? Unchecked.<ZipInputStream>cast(inputStream) : new ZipInputStream(inputStream);
    }

    public static ZipSource zipSource(InputStream inputStream) {
        return new ZipSource(inputStream);
    }

    @Override
    public Sequence<Source> sources() {
        return Zip.entries(in).map(new Function1<ZipEntry, Source>() {
            @Override
            public Source call(ZipEntry zipEntry) throws Exception {
                return new Source(zipEntry.getName(), new Date(zipEntry.getTime()), new IgnoreCloseInputStream());
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
