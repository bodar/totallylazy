package com.googlecode.totallylazy;

import java.io.File;

public class Files {
    public static Callable1<File, String> getName() {
         return new Callable1<File, String>() {
             public String call(File file) throws Exception {
                 return file.getName();
             }
         };
     }

     public static Callable1<? super File, File> getParentFile() {
         return new Callable1<File, File>() {
             public File call(File file) throws Exception {
                 return file.getParentFile();
             }
         };
     }

    public static void ensureDirectoryExists(File directory){
        if (directory.exists()) {
            if (directory.isFile()) {
                String message =
                    "File "
                        + directory
                        + " exists and is "
                        + "not a directory. Unable to create directory.";
                throw new RuntimeException(message);
            }
        } else {
            if (false == directory.mkdirs()) {
                String message =
                    "Unable to create directory " + directory;
                throw new RuntimeException(message);
            }
        }
    }
}
