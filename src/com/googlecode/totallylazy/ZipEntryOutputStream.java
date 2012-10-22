package com.googlecode.totallylazy;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipEntryOutputStream extends OutputStream {
    private final ZipOutputStream out;

    public ZipEntryOutputStream(final ZipOutputStream out, String filename, Date modified) throws IOException {
        this.out = out;
        ZipEntry entry = new ZipEntry(filename);
        entry.setTime(modified.getTime());
        this.out.putNextEntry(entry);
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