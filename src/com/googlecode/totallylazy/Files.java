package com.googlecode.totallylazy;

import java.io.File;

import static java.lang.System.getProperty;
import static java.util.UUID.randomUUID;

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

    public static File temporaryFile(){
        File file = new File(getProperty("java.io.tmpdir"), randomUUID().toString());
        file.deleteOnExit();
        return file;
    }

    public static File temporaryDirectory(){
        File file = temporaryFile();
        file.mkdirs();
        return file;
    }
}
