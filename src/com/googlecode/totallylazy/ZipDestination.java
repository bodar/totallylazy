package com.googlecode.totallylazy;

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipOutputStream;

public class ZipDestination implements Destination {
    private final ZipOutputStream zipOutputStream;

    private ZipDestination(final ZipOutputStream zipOutputStream) {
        this.zipOutputStream = zipOutputStream;
    }

    public static ZipDestination zipDestination(OutputStream outputStream) {
        return new ZipDestination(outputStream instanceof ZipOutputStream ? (ZipOutputStream) outputStream :
                new ZipOutputStream(outputStream));
    }

    @Override
    public OutputStream destination(String name) throws IOException {
        return new ZipEntryOutputStream(zipOutputStream, name);
    }

    @Override
    public void close() throws IOException {
        zipOutputStream.close();
    }
}
