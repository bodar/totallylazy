package com.googlecode.totallylazy;

import java.io.File;
import java.io.IOException;

import static java.lang.System.getProperty;
import static java.util.UUID.randomUUID;

public class Files {
    public static final File TEMP_DIR = new File(getProperty("java.io.tmpdir"));

    public static Callable1<File, String> name() {
        return new Callable1<File, String>() {
            public String call(File file) throws Exception {
                return file.getName();
            }
        };
    }

    public static Callable1<? super File, File> parent() {
        return new Callable1<File, File>() {
            public File call(File file) throws Exception {
                return file.getParentFile();
            }
        };
    }

    public static File temporaryDirectory() {
        File directory = new File(TEMP_DIR, randomFilename());
        directory.deleteOnExit();
        directory.mkdirs();
        return directory;
    }

    public static File temporaryFile() {
        return temporaryFile(TEMP_DIR);
    }

    public static File temporaryFile(File directory) {
        try {
            File file = new File(directory, randomFilename());
            file.deleteOnExit();
            file.createNewFile();
            return file;
        } catch (IOException e) {
            throw new LazyException(e);
        }
    }

    public static String randomFilename() {
        return randomUUID().toString();
    }

    public static Sequence<File> files(File directory) {
        return Sequences.sequence(directory.listFiles());
    }
}
