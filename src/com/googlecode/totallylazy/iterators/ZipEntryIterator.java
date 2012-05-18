package com.googlecode.totallylazy.iterators;

import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipEntryIterator extends NullTerminatingIterator<ZipEntry> {
    private final ZipInputStream input;

    public ZipEntryIterator(ZipInputStream input) {
        this.input = input;
    }

    @Override
    protected ZipEntry nextNullTerminating() throws Exception {
        return input.getNextEntry();
    }
}
