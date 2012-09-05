package com.googlecode.totallylazy;

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipEntryOutputStream extends OutputStream {
    private final ZipOutputStream out;

    public ZipEntryOutputStream(final ZipOutputStream out, String filename) throws IOException {
        this.out = out;
        this.out.putNextEntry(new ZipEntry(filename));
    }

    @Override
    public void write(int b) throws IOException {
        out.write(b);
    }

    @Override
    public void write(byte[] b) throws IOException {
        out.write(b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        out.write(b, off, len);
    }

    @Override
    public void close() throws IOException {
        out.closeEntry();
    }
}