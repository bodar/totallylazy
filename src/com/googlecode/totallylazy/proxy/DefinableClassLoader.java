package com.googlecode.totallylazy.proxy;

/**
 * Created by dan on 29/06/15.
 */
class DefinableClassLoader extends ClassLoader {
    public Class<?> defineClass(String name, byte[] b) {
        return defineClass(name, b, 0, b.length);
    }
}
