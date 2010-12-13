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
}
