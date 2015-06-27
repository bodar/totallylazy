package com.googlecode.totallylazy.io;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipEntryOutputStream extends OutputStream {
    private final ZipOutputStream out;
    private final String filename;
    private final Date modified;
    private boolean init = true;

    public ZipEntryOutputStream(final ZipOutputStream out, String filename, Date modified) throws IOException {
        this.out = out;
        this.filename = filename;
        this.modified = modified;
    }

    private void init(boolean zeroBytes) throws IOException {
        if (init) {
            init = false;
            ZipEntry entry = new ZipEntry(filename);
            entry.setTime(modified.getTime());
            if (zeroBytes) {
                entry.setMethod(ZipEntry.STORED);
                entry.setSize(0);
                entry.setCrc(0);
            }
            this.out.putNextEntry(entry);
        }
    }

    @Override
    public void write(int b) throws IOException {
        init(false);
        out.write(b);
    }

    @Override
    public void write(byte[] b) throws IOException {
        init(false);
        out.write(b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        init(false);
        out.write(b, off, len);
    }

    @Override
    public void close() throws IOException {
        init(true);
        out.closeEntry();
    }
}