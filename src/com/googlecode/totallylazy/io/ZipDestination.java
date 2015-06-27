package com.googlecode.totallylazy.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.jar.JarOutputStream;
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

    public static ZipDestination jarFile(File jarFile) throws IOException {
        return zipDestination(new JarOutputStream(new FileOutputStream(jarFile)));
    }

    @Override
    public OutputStream destination(String name, Date modified) throws IOException {
        return new ZipEntryOutputStream(zipOutputStream, name, modified);
    }

    @Override
    public void close() throws IOException {
        zipOutputStream.close();
    }
}
