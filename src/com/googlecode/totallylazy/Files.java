package com.googlecode.totallylazy;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static com.googlecode.totallylazy.Sequences.sequence;
import static java.lang.System.getProperty;
import static java.util.UUID.randomUUID;

public class Files {
    public static final File TEMP_DIR = new File(getProperty("java.io.tmpdir"));

    public static Predicate<? super File> isFile() {
        return new Predicate<File>() {
            public boolean matches(File file) {
                return file.isFile();
            }
        };
    }

    public static Predicate<? super File> isDirectory() {
        return new Predicate<File>() {
            public boolean matches(File file) {
                return file.isDirectory();
            }
        };
    }

    public static Callable1<File, String> name() {
        return new Callable1<File, String>() {
            public String call(File file) throws Exception {
                return file.getName();
            }
        };
    }

    public static Callable1<? super File, String> path() {
        return new Callable1<File, String>() {
            public String call(File file) throws Exception {
                return file.getPath();
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
        return temporaryDirectory(randomFilename());
    }

    public static File temporaryDirectory(String name) {
        File directory = new File(TEMP_DIR, name);
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

    public static File workingDirectory() {
        return new File(".");
    }

    public static String randomFilename() {
        return randomUUID().toString();
    }

    public static Sequence<File> files(File directory) {
        return Sequences.sequence(directory.listFiles());
    }

    public static Sequence<File> recursiveFiles(final File directory){
        return files(directory).flatMap(recursiveFiles());
    }

    public static Callable1<File, Iterable<File>> recursiveFiles() {
        return new Callable1<File, Iterable<File>>() {
            public Iterable<File> call(File file) throws Exception {
                return file.isDirectory() ? sequence(file).join(recursiveFiles(file)) : sequence(file);
            }
        };
    }

    public static File write(byte[] bytes, File file) throws IOException {
        OutputStream outputStream = new FileOutputStream(file);
        outputStream.write(bytes);
        outputStream.close();
        return file;
    }

    public static Callable1<? super File, Boolean> delete() {
        return new Callable1<File, Boolean>() {
            public Boolean call(File file) throws Exception {
                return file.delete();
            }
        };
    }

    public static Callable1<? super String,File> asFile() {
        return new Callable1<String, File>() {
            public File call(String name) throws Exception {
                return new File(name);
            }
        };
    }
}
