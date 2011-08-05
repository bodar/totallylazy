package com.googlecode.totallylazy;

import java.io.IOException;
import java.io.OutputStream;

public class Streams {
    public static OutputStream nullOutputStream() {
        return new OutputStream() {
            @Override
            public void write(int b) throws IOException {
            }
        };
    }
}
