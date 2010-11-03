package com.googlecode.totallylazy;

public class Strings {
    public static Callable1<String, String> toLowerCase() {
        return new Callable1<String, String>() {
            public String call(String value) throws Exception {
                return value.toLowerCase();
            }
        };
    }

    public static Callable1<String, String> toUpperCase() {
        return new Callable1<String, String>() {
            public String call(String value) throws Exception {
                return value.toUpperCase();
            }
        };
    }
}
